package ru.skillbox.diplom.group42.social.service.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.Post_;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag_;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final TagService tagService;
    private final LikeRepository likeRepository;

    public PostDto getById(Long id) {
        log.info("start PostService getById " + id);
        return postMapper.convertToDTO(postRepository.findById(id).orElseThrow(PostFoundException::new));
    }
//TODO на фронте в разделе профиль не отображаются отложенные посты
    public Page<PostDto> getAll(PostSearchDto postSearchDto, Pageable pageable) {
        log.info("start PostService getAll " + postSearchDto + " Pageable " + pageable);
        if (postSearchDto.getDateTo() == null) {
            postSearchDto.setDateTo(ZonedDateTime.now());
        }

        Page<Post> postList = postRepository.findAll(getPostSpecification(postSearchDto), pageable);

        return new PageImpl<>(postList.map(post -> {
            PostDto postDto = postMapper.convertToDTO(post);
            postDto.setTags(tagMapper.convertSetToDto(post.getTags()));
            postDto.setMyLike(likeRepository.existsByAuthorIdAndItemId(SecurityUtil.getJwtUserIdFromSecurityContext(), postDto.getId()));
            log.info("finish PostService getAll " + postDto + " Pageable " + pageable);
            return postDto;
        }).toList(), pageable, postList.getTotalElements());
    }

    public PostDto create(PostDto postDto) {
        log.info("start PostService  create Post: " + postDto.toString());
        Post post = postMapper.createEntity(postDto);
        post.setTags(tagService.create(postDto.getTags()));
        PostDto postDto1 = postMapper.convertToDTO(postRepository.save(post));
        postDto1.setIsBlocked(false);
        postDto1.setTags(tagMapper.convertSetToDto(post.getTags()));
        return postDto1;
    }

    public PostDto update(PostDto postDto) {
        log.info("start PostService  update Post: " + postDto.toString());
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

    public void deleteById(Long id) {
        postRepository.deleteById(id);
        log.info("start PostService deleteById Post: " + id);

    }
//TODO не нужен, реализуется через create и SpecificationUtil.between(Post_.publishDate....) по сваггеру PUT /api/v1/post/delayed
    public void delayedPost(PostDto postDto) {
        Post post = postMapper.createEntity(postDto);
        post.setTags(tagService.create(postDto.getTags()));
        postRepository.save(post);

        log.info("start PostService delayedPost: " + postDto.toString());
    }

    private Specification<Post> getPostSpecification(PostSearchDto postSearchDto) {
        return SpecificationUtil.getBaseSpecification(postSearchDto)
                .and(SpecificationUtil.in(Post_.id, postSearchDto.getIds(), true))
                .and(SpecificationUtil.in(Post_.authorId, postSearchDto.getAccountIds(), true))
                .and(SpecificationUtil.notIn(Post_.authorId, postSearchDto.getBlockedIds(), true))
                .and(SpecificationUtil.between(Post_.publishDate, postSearchDto.getDateFrom(), postSearchDto.getDateTo(), true))
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
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(singularAttribute)), "%" + splitText[i].trim().toLowerCase() + "%");
                predicateList.add(predicate);
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicates = predicateList.toArray(predicates);
            return criteriaBuilder.or(predicates);
        };
    }
}
