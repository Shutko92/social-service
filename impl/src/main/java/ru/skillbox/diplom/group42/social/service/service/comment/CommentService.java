package ru.skillbox.diplom.group42.social.service.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentType;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment_;
import ru.skillbox.diplom.group42.social.service.exception.CommentFoundException;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.service.like.LikeService;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final LikeService likeService;
    private final NotificationHandler notificationHandler;

    /**
     * Метод ищет все совпадающие комментарии по спецификации и идентификатору из параметров. Полученные комментарии
     * конвертируются в страничный список dto используя обращение к сервису реакций.
     * @param postId идентификатор публикации.
     * @param commentSearchDto не используется.
     * @param pageable парамерт разделения на страницы.
     * @return страничная информация о комментариях.
     */
    public Page<CommentDto> getAllCommentsToPost(Long postId, CommentSearchDto commentSearchDto, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(getSpecificationForComment(postId), pageable);
        return new PageImpl<>(commentPage.map(comment -> {
            CommentDto commentDto = commentMapper.convertToDto(comment);
            commentDto.setMyLike(likeService.isThereMyLikeToComment(comment.getId(), TypeLike.COMMENT));
            return commentDto;
        }).toList(), pageable, commentPage.getTotalElements());
    }

    /**
     * Метод ищет все совпадающие комментарии по спецификации и идентификатору из параметров. Результат конвертируется в ответ.
     * @param postId не используется.
     * @param commentId идентификатор комментария.
     * @param commentSearchDto не используется.
     * @param pageable парамерт разделения на страницы.
     * @return страничная информация о комментариях.
     */
    public Page<CommentDto> getSubcomment(Long postId, Long commentId, CommentSearchDto commentSearchDto, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(getSpecificationForSubcomment(commentId), pageable);
        return new PageImpl<>(commentPage.map(commentMapper::convertToDto).toList(), pageable, commentPage.getTotalElements());
    }

    /**
     * Метод выставляет тип комментаия и идентификатор, если имеется идентификатор публикации. Также ищет публикацию по
     * идентификатору, или выбрасывает исключение. Добавляет комментарий и сохраняет публикацию. Сохраняет комментарий, формирует
     * ответ, отправляет нотификацию. Если идентификатора публикации нет, вызывает другой метод и возвращает результат.
     * @param postId идентификатор публикации.
     * @param dto параметры комментария.
     * @return
     */
    public CommentDto create(Long postId, CommentDto dto) {
        if (dto.getParentId() == null) {
            dto.setCommentType(CommentType.POST);
            dto.setPostId(postId);
            Post post = postRepository.findById(postId).orElseThrow(PostFoundException::new);
            post.setCommentsCount(post.getCommentsCount() + 1);
            postRepository.save(post);
            dto = commentMapper.convertToDto(commentRepository.save(commentMapper.createEntity(dto)));
            notificationHandler.sendNotifications(dto.getAuthorId(), NotificationType.POST_COMMENT, dto.getCommentText());
            return dto;
        } else {
            return createSubComment(postId, dto.getParentId(), dto);
        }
    }

    /**
     * Метод выставляет тип комментария в параметры, ищет комментарий по идентификатору или выбрасывает исключение, конвертирует
     * информацию и актуализирует счетчик, создает комментарий из параметров, сохраняет комментарий и саб-комментарий,
     * отправляет нотификацию.
     * @param id идентификатор комментария.
     * @param commentId идентификатор комментария.
     * @param dto параметры комметария.
     * @return информация о комментарии.
     */
    public CommentDto createSubComment(Long id, Long commentId, CommentDto dto) {
        dto.setCommentType(CommentType.COMMENT);
        CommentDto parentComment = commentMapper.convertToDto(commentRepository.findById(commentId).orElseThrow(CommentFoundException::new));
        parentComment.setCommentCount(parentComment.getCommentCount() + 1);
        Comment comment = commentMapper.createEntity(dto);
        comment.setPostId(id);
        commentRepository.save(commentMapper.convertToEntity(parentComment));
        dto = commentMapper.convertToDto(commentRepository.save(comment));
        notificationHandler.sendNotifications(dto.getAuthorId(), NotificationType.COMMENT_COMMENT, dto.getCommentText());
        return dto;
    }

    /**
     * Метод ищет комментарий по идентификатору или выбрасывает исключение, обновляет его, сохраняет, конвертирует в ответ.
     * @param id не используется.
     * @param dto параметры комметария.
     * @return информация о комментарии.
     */
    public CommentDto update(Long id, CommentDto dto) {
        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(CommentFoundException::new);
        comment.setCommentText(dto.getCommentText());
        comment.setTimeChanged(ZonedDateTime.now());
        return commentMapper.convertToDto(commentRepository.save(comment));
    }

    /**
     * Метод ищет комментарий через репозиторий по идентификатору. Если есть идентификатор публикации, ищет ее через репозиторий,
     * удаляет комментарий у публикации, сохраняет результат. Иначе, удаляет комментарий, сохраняет результат.
     * @param id идентификатор комментария.
     * @param commentId идентификатор комментария.
     */
    public void deleteById(Long id, Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getParentId() == null) {
            Post post = postRepository.findById(id).orElseThrow(PostFoundException::new);
            post.setCommentsCount(post.getCommentsCount() - 1);
            postRepository.save(post);
        } else {
            comment.setCommentCount(comment.getCommentCount() - 1);
            commentRepository.save(comment);
        }
        commentRepository.deleteById(commentId);
    }

    private Specification<Comment> getSpecificationForComment(Long postId) {
        return SpecificationUtil.getBaseSpecification(new CommentSearchDto())
                .and(SpecificationUtil.equal(Comment_.parentId, null, true))
                .and(SpecificationUtil.equal(Comment_.postId, postId, true))
                .and(SpecificationUtil.equal(Comment_.commentType, CommentType.POST, true))
                .and(SpecificationUtil.equal(Comment_.isDeleted, false, true));
    }

    private Specification<Comment> getSpecificationForSubcomment(Long parentId) {
        return SpecificationUtil.getBaseSpecification(new CommentSearchDto())
                .and(SpecificationUtil.equal(Comment_.parentId, parentId, true))
                .and(SpecificationUtil.equal(Comment_.commentType, CommentType.COMMENT, true))
                .and(SpecificationUtil.equal(Comment_.isDeleted, false, true));
    }

}
