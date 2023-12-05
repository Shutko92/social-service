package ru.skillbox.diplom.group42.social.service.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.Type;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.Post_;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag_;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group42.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.service.friend.FriendService;
import ru.skillbox.diplom.group42.social.service.service.like.LikeService;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.service.tag.TagService;
import ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final TagService tagService;
    private final LikeRepository likeRepository;
    private final LikeService likeService;
    private final NotificationHandler notificationHandler;
    private final UserRepository userRepository;
    private final FriendService friendService;

    /**
     * Метод ищет публикацию по идентификатору и возвращает конвертированную информацию о ней, если публикация находится.
     *
     * @param id идентификатор публикации.
     * @return информация о публикации.
     */
    public PostDto getById(Long id) {
        return postMapper.convertToDTO(postRepository.findById(id).orElseThrow(PostFoundException::new));
    }

    /**
     * Метод проводит поиск по параметрам запроса и конвертирует информацию в ответ.
     *
     * @param postSearchDto параметры запроса для поиска публикации.
     * @param pageable      пагинация для постраничного формирования ответа.
     * @return информация о публикациях, разделенная на страницы.
     */
    public Page<PostDto> getAll(PostSearchDto postSearchDto, Pageable pageable) {

        Page<Post> postList = postRepository.findAll(getPostSpecification(getAuthorsIds(postSearchDto)), pageable);

        return new PageImpl<>(postList.map(post -> {
            if (post.getPublishDate().isBefore(ZonedDateTime.now())) {
                updateTypePost(post);
            }
            PostDto postDto = postMapper.convertToDTO(post);
            postDto.setTags(tagMapper.convertSetToDto(post.getTags()));
            postDto.setMyLike(likeRepository.existsByAuthorIdAndItemId(SecurityUtil.getJwtUserIdFromSecurityContext(), postDto.getId()));
            postDto.setReactions(likeService.getSetReactionDto(post.getId()));
            postDto.setMyReaction(likeService.getMyReaction(post.getId(), TypeLike.POST));
            return postDto;
        }).toList(), pageable, postList.getTotalElements());
    }

    /**
     * Метод конвертирует параметры запроса в сущность, которую сохраняет. Отправляет нотификацию об отправленной публикации.
     *
     * @param postDto параметры запроса для создания публикации.
     * @return информация о публикации.
     */
    public PostDto create(PostDto postDto) {
        Post post = postMapper.createEntity(postDto);
        post.setTags(tagService.create(postDto.getTags()));
        post.setType(post.getPublishDate().isAfter(ZonedDateTime.now()) ? Type.QUEUED : Type.POSTED);
        PostDto postDto1 = postMapper.convertToDTO(postRepository.save(post));
        postDto1.setIsBlocked(false);
        postDto1.setTags(tagMapper.convertSetToDto(post.getTags()));
        notificationHandler.sendNotifications(post.getAuthorId(), NotificationType.POST, postDto1.getTitle());
        return postDto1;
    }

    /**
     * Метод ищет публикацию по параметрам запроса, обновляет данные, сохраняет изменения.
     *
     * @param postDto параметры запроса для обновления публикации.
     * @return информация о публикации.
     */
    public PostDto update(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId()).get();
        post.setPostText(postDto.getPostText());
        post.setTags(tagService.create(postDto.getTags()));
        post.setTitle(postDto.getTitle());
        post.setImagePath(postDto.getImagePath());
        post.setTimeChanged(ZonedDateTime.now());
        PostDto postDto1 = postMapper.convertToDTO(postRepository.save(post));
        postDto1.setTags(tagMapper.convertSetToDto(post.getTags()));
        return postDto1;
    }

    /**
     * Метод удаляет публикацию по указанному идентификатору.
     *
     * @param id идентификатор публикации
     */
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    private Specification<Post> getPostSpecification(PostSearchDto postSearchDto) {
        return SpecificationUtil.getBaseSpecification(postSearchDto)
                .and(SpecificationUtil.in(Post_.id, postSearchDto.getIds(), true))
                .and(SpecificationUtil.in(Post_.authorId, postSearchDto.getAccountIds(), true))
                .and(SpecificationUtil.notIn(Post_.authorId, postSearchDto.getBlockedIds(), true))
                .and(SpecificationUtil.between(Post_.publishDate,
                        postSearchDto.getDateFrom() == null ? null : ZonedDateTime.parse(postSearchDto.getDateFrom())
                        , postSearchDto.getDateTo() == null ? null : ZonedDateTime.parse(postSearchDto.getDateTo()), true))
                .and(getContainsTag(postSearchDto.getTags())).and(SpecificationUtil.equal(Post_.isDeleted, false, true))
                .and(getSpecificationSearchRequest(Post_.title, postSearchDto.getText()))
                .or(getSpecificationSearchRequest(Post_.postText, postSearchDto.getText()));
    }

    private Specification<Post> getContainsTag(List<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null) {
                return null;
            }
            HashSet<String> convertToSet = new HashSet<>(tags);
            Join<Post, Tag> tagsJoin = root.join(Post_.tags);
            return criteriaBuilder.in(tagsJoin.get(Tag_.NAME)).value(convertToSet);
        };
    }

    private Specification<Post> getSpecificationSearchRequest(SingularAttribute<Post, String> singularAttribute, String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null) {
                return null;
            }
            ArrayList<Predicate> predicateList = new ArrayList<>();
            String[] splitText = text.split(" ");
            for (int i = 0; i < splitText.length; i++) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(singularAttribute))
                        , "%" + splitText[i].trim().toLowerCase() + "%");
                predicateList.add(predicate);
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicates = predicateList.toArray(predicates);
            return criteriaBuilder.or(predicates);
        };
    }

    private Post updateTypePost(Post post) {
        post.setType(Type.POSTED);
        return postRepository.save(post);
    }

    private PostSearchDto getAuthorsIds(PostSearchDto postSearchDto) {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setIdFrom(SecurityUtil.getJwtUserIdFromSecurityContext());
        friendSearchDto.setIsDeleted(false);
        if (postSearchDto.getWithFriends() == null) {
            postSearchDto.setWithFriends(true);
        }
        if (postSearchDto.getAuthor() != null) {
            return getSearchByAuthor(postSearchDto, friendSearchDto);
        }
        friendSearchDto.setStatusCode(StatusCode.FRIEND.toString());
        if (postSearchDto.getWithFriends()) {
            Page<FriendShortDto> friendsIds = friendService.getFriends(friendSearchDto, Pageable.unpaged());
            if (friendsIds != null && postSearchDto.getAccountIds() == null) {
                postSearchDto.setAccountIds(friendsIds.stream().map(FriendShortDto::getFriendId).collect(Collectors.toList()));
                postSearchDto.setAccountIds(addWatchingIds(friendSearchDto, postSearchDto));
            }
        }
        postSearchDto.setBlockedIds(addBlockedIds(friendSearchDto));
        return postSearchDto;
    }

    private List<Long> addWatchingIds(FriendSearchDto friendSearchDto, PostSearchDto postSearchDto) {
        friendSearchDto.setStatusCode(StatusCode.WATCHING.toString());
        List<Long> watchingIds = friendService.getFriends(friendSearchDto, Pageable.unpaged())
                .stream().map(FriendShortDto::getFriendId).collect(Collectors.toList());
        List<Long> tmpAccountIdsList = postSearchDto.getAccountIds();
        tmpAccountIdsList.addAll(watchingIds);
        tmpAccountIdsList.add(SecurityUtil.getJwtUserIdFromSecurityContext());
        return tmpAccountIdsList;
    }

    private PostSearchDto getSearchByAuthor(PostSearchDto postSearchDto, FriendSearchDto friendSearchDto) {
        List<Long> result = new ArrayList<>();
        String[] name = postSearchDto.getAuthor().trim().split(" ");
        List<Long> usersIds = userRepository.findAllByFirstNameIgnoreCaseOrLastNameIgnoreCase(name[0], name.length > 1 ? name[1] : null)
                .stream().map(user -> user.getId()).collect(Collectors.toList());
        postSearchDto.setAccountIds(usersIds);
        usersIds.forEach(userId -> {
            friendSearchDto.setIdTo(userId);
            result.addAll(friendService.getFriends(friendSearchDto, Pageable.unpaged())
                    .stream().filter(friendShortDto -> friendShortDto.getStatusCode().equals(StatusCode.FRIEND)
                            || friendShortDto.getStatusCode().equals(StatusCode.WATCHING)).map(FriendShortDto::getFriendId)
                    .collect(Collectors.toList()));
        });
        postSearchDto.setAccountIds(result);
        postSearchDto.setBlockedIds(addBlockedIds(friendSearchDto));
        return postSearchDto;
    }

    private List<Long> addBlockedIds(FriendSearchDto friendSearchDto) {
        friendSearchDto.setStatusCode(StatusCode.BLOCKED.toString());

        List<Long> blockedIds = friendService.getFriends(friendSearchDto, Pageable.unpaged()) != null ?
                friendService.getFriends(friendSearchDto, Pageable.unpaged()).stream().map(FriendShortDto::getFriendId)
                        .collect(Collectors.toList()) : new ArrayList<>();
        if (!blockedIds.isEmpty()) {
            return blockedIds;
        } else {
            return null;
        }
    }

}
