package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.service.comment.CommentService;
import ru.skillbox.diplom.group42.social.service.service.like.LikeService;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createComment;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createCommentSearchDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private LikeService likeService;
    @Mock
    private NotificationHandler notificationHandler;

    private CommentService commentService;

    @BeforeEach
    public void beforeMethod() {
        commentService = new CommentService(commentRepository, postRepository, commentMapper, likeService, notificationHandler);
    }

    @Test
    public void funGetAllCommentsToPostShouldInvokeCommentRepositoryFindAll() {
        CommentSearchDto searchDto = createCommentSearchDto();
        List<Comment> list = new ArrayList<>();
        when(commentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl(list));
        commentService.getAllCommentsToPost(TEST_ID, searchDto, Pageable.ofSize(10));
        verify(commentRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void funGetSubcommentShouldInvokeCommentRepositoryFindAll() {
        CommentSearchDto searchDto = createCommentSearchDto();
        List<Comment> list = new ArrayList<>();
        when(commentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl(list));
        Page<CommentDto> page = commentService.getSubcomment(TEST_ID, TEST_ID, searchDto, Pageable.ofSize(1));
        assert page != null;
        verify(commentRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void funCreateShouldInvokePostRepositoryFindByIdAndSave() {
        CommentDto commentDto = MappingTestingDataFactory.createCommentDto(TEST_ID);
        commentDto.setPostId(TEST_ID);
        commentDto.setParentId(null);
        Post post = new Post();
        post.setCommentsCount(10);
        when(postRepository.findById(TEST_ID)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment());
        when(commentMapper.convertToDto(any(Comment.class))).thenReturn(commentDto);
        when(commentMapper.createEntity(any(CommentDto.class))).thenReturn(new Comment());
        commentService.create(TEST_ID, commentDto);
        verify(postRepository, times(1)).findById(anyLong());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void funCreateShouldInvokeCreateSubCommentWhenParentIdNotNull() {
        CommentDto commentDto = MappingTestingDataFactory.createCommentDto(TEST_ID);
        commentDto.setPostId(TEST_ID);
        commentDto.setParentId(TEST_ID);
        Comment comment = createComment(TEST_ID);
        when(commentMapper.convertToDto(any(Comment.class))).thenReturn(commentDto);
        when(commentMapper.createEntity(any(CommentDto.class))).thenReturn(comment);
        when(commentMapper.convertToEntity(any(CommentDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        commentService.create(TEST_ID, commentDto);
        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    public void funUpdateShouldInvokeCommentRepositorySave() {
        CommentDto commentDto = MappingTestingDataFactory.createCommentDto(TEST_ID);
        Comment comment = createComment(TEST_ID);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        commentService.update(TEST_ID, commentDto);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void funDeleteByIdShouldInvokeCommentRepositorySaveWhenParentIdNull() {
        Comment comment = createComment(TEST_ID);
        comment.setParentId(null);
        Post post = new Post();
        post.setCommentsCount(10);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        commentService.deleteById(TEST_ID, TEST_ID);
        verify(postRepository).save(any(Post.class));
        verify(commentRepository).deleteById(anyLong());
    }

    @Test
    public void funDeleteByIdShouldInvokeCommentRepositorySaveWhenParentIdNotNull() {
        Comment comment = createComment(TEST_ID);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        commentService.deleteById(TEST_ID, TEST_ID);
        verify(commentRepository).save(any(Comment.class));
        verify(commentRepository).deleteById(anyLong());
    }
}
