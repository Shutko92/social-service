package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.service.like.LikeService;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationService;
import ru.skillbox.diplom.group42.social.service.service.post.PostService;
import ru.skillbox.diplom.group42.social.service.service.tag.TagService;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createPost;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createPostDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createPostSearchDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostMapper postMapper;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private TagService tagService;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private LikeService likeService;
    @Mock
    private  NotificationHandler notificationHandler;
    private PostService postService;

    @BeforeEach
    public void beforeMethod() {
        postService = new PostService(postRepository, postMapper, tagMapper, tagService, likeRepository, likeService, notificationHandler);
    }

    @Test
    public void funGetByIdShouldInvokePostRepositoryFindById() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(new Post()));
        postService.getById(TEST_ID);
        verify(postRepository).findById(anyLong());
    }

    @Test
    public void funGetAllShouldInvokeAllNeedFunction() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            List<Post> postList = new ArrayList<>();
            postList.add(createPost(TEST_ID));
            Page<Post> page = new PageImpl<>(postList);
            when(postRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
            when(postMapper.convertToDTO(any(Post.class))).thenReturn(createPostDto(TEST_ID));
            PostSearchDto postSearchDto = createPostSearchDto();
            postService.getAll(postSearchDto, Pageable.ofSize(1));
            verify(postRepository).findAll(any(Specification.class), any(Pageable.class));
        }
    }

    @Test
    public void funCreateShouldInvokePostRepositorySave() {
        PostDto postDto = createPostDto(TEST_ID);
        Post post = createPost(TEST_ID);
        when(postMapper.createEntity(any(PostDto.class))).thenReturn(post);
        when(postMapper.convertToDTO(any(Post.class))).thenReturn(postDto);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        postService.create(postDto);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    public void funUpdateShouldInvokePostRepositorySave() {
        Post post = createPost(TEST_ID);
        PostDto postDto = createPostDto(TEST_ID);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postMapper.convertToDTO(any(Post.class))).thenReturn(postDto);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        postService.update(postDto);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    public void deleteByIdTest() {
        postService.deleteById(TEST_ID);
        verify(postRepository).deleteById(anyLong());
    }
}
