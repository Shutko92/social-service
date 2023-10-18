package ru.skillbox.diplom.group42.social.service.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentType;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment_;
import ru.skillbox.diplom.group42.social.service.exception.CommentFoundException;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public Page<CommentDto> getAllCommentsToPost(Long postId, CommentSearchDto commentSearchDto, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(getSpecificationForComment(postId), pageable);
        return new PageImpl<>(commentPage.map(comment -> {
            CommentDto commentDto = commentMapper.convertToDto(comment);

         return commentDto;
        }).toList(), pageable, commentPage.getTotalElements());
    }

    public Page<CommentDto> getSubcomment(Long postId, Long commentId, CommentSearchDto commentSearchDto, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(getSpecificationForSubcomment(commentId), pageable);
        return new PageImpl<>(commentPage.map(commentMapper::convertToDto).toList(), pageable, commentPage.getTotalElements());
    }

    public CommentDto create(Long postId, CommentDto dto) {
        if (dto.getParentId() == null) {
            dto.setCommentType(CommentType.POST);
            dto.setPostId(postId);
            Post post = postRepository.findById(postId).orElseThrow(PostFoundException::new);
            post.setCommentsCount(post.getCommentsCount() + 1);
            postRepository.save(post);
            return commentMapper.convertToDto(commentRepository.save(commentMapper.createEntity(dto)));
        } else {
            return createSubComment(postId, dto.getParentId(), dto);
        }
    }

    public CommentDto createSubComment(Long id, Long commentId, CommentDto dto) {
        dto.setCommentType(CommentType.COMMENT);
        CommentDto parentComment = commentMapper.convertToDto(commentRepository.findById(commentId).orElseThrow(CommentFoundException::new));
        parentComment.setCommentCount(parentComment.getCommentCount() + 1);
        commentRepository.save(commentMapper.createEntity(parentComment));
        return commentMapper.convertToDto(commentRepository.save(commentMapper.createEntity(dto)));
    }


    public CommentDto update(Long id, CommentDto dto) {
        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(CommentFoundException::new);
        comment.setCommentText(dto.getCommentText());
        comment.setTimeChanged(ZonedDateTime.now());
        return commentMapper.convertToDto(commentRepository.save(comment));
    }

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
                .and(SpecificationUtil.equal(Comment_.parentId,null,true))
                .and(SpecificationUtil.equal(Comment_.postId, postId, true))
                .and(SpecificationUtil.equal(Comment_.commentType,CommentType.POST, true))
                .and(SpecificationUtil.equal(Comment_.isDeleted, false, true));
    }

    private Specification<Comment> getSpecificationForSubcomment(Long parentId){
        return SpecificationUtil.getBaseSpecification(new CommentSearchDto())
                .and(SpecificationUtil.equal(Comment_.parentId,parentId,true))
                .and(SpecificationUtil.equal(Comment_.commentType, CommentType.COMMENT, true))
                .and(SpecificationUtil.equal(Comment_.isDeleted,false,true));
    }

}
