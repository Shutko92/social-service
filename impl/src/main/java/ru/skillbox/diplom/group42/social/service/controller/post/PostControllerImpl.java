package ru.skillbox.diplom.group42.social.service.controller.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.service.comment.CommentService;
import ru.skillbox.diplom.group42.social.service.service.like.LikeService;
import ru.skillbox.diplom.group42.social.service.service.post.PostService;

@Slf4j
@RestController
@RequiredArgsConstructor

public class PostControllerImpl implements PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Override
    public ResponseEntity<PostDto> getById(Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @Override
    public ResponseEntity<Page<PostDto>> getAll(PostSearchDto postSearchDTO, Pageable pageable) {
        return ResponseEntity.ok(postService.getAll(postSearchDTO, pageable));
    }

    @Override
    public ResponseEntity<PostDto> create(PostDto dto) {
        return ResponseEntity.ok(postService.create(dto));
    }

    @Override
    public ResponseEntity<PostDto> update(PostDto dto) {
        return ResponseEntity.ok(postService.update(dto));
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        postService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


//-------------------------=Comment=-------------------------//

    @Override
    public ResponseEntity<Page<CommentDto>> getAllCommentsToPost(Long postId, CommentSearchDto commentSearchDto, Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllCommentsToPost(postId, commentSearchDto, pageable));
    }

    @Override
    public ResponseEntity<Page<CommentDto>> getSubcomment(Long postId, Long commentId, CommentSearchDto commentSearchDto, Pageable pageable) {
        return ResponseEntity.ok(commentService.getSubcomment(postId, commentId, commentSearchDto, pageable));
    }

    @Override
    public ResponseEntity<CommentDto> create(Long postId, CommentDto dto) {
        return ResponseEntity.ok(commentService.create(postId, dto));
    }

    @Override
    public ResponseEntity<CommentDto> createSubComment(Long id, Long commentId, CommentDto dto) {
        return ResponseEntity.ok(commentService.createSubComment(id, commentId, dto));
    }

    @Override
    public ResponseEntity<CommentDto> update(Long id, CommentDto dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @Override
    public ResponseEntity deleteById(Long id, Long commentId) {
        commentService.deleteById(id, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    //--------------------------=Reaction=-----------------------------//

    @Override
    public ResponseEntity<LikeDto> createPostReaction(Long id, LikeDto likeDto) {
        return ResponseEntity.ok(likeService.addLike(id, likeDto, TypeLike.POST));
    }

    @Override
    public ResponseEntity deletePostReaction(Long id) {
        likeService.deleteLike(id, TypeLike.POST);
        return ResponseEntity.ok().build();
    }

    //--------------------------=Like=-----------------------------//
    @Override
    public ResponseEntity<LikeDto> createCommentLike(Long id, LikeDto likeDto, Long commentId) {
        return ResponseEntity.ok(likeService.addLike(commentId, likeDto, TypeLike.COMMENT));
    }

    @Override
    public ResponseEntity deleteCommentLike(Long id, Long commentId) {
        likeService.deleteLike(commentId, TypeLike.COMMENT);
        return ResponseEntity.ok().build();
    }

}

