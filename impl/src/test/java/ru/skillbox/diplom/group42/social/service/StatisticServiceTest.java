package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticRequestDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticType;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.mapper.admin.console.AdminConsoleMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.service.admin.console.StatisticService;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createStatisticRequestDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_SECOND_ID;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AdminConsoleMapper adminConsoleMapper;
    private StatisticService statisticService;
    private StatisticResponseDto response = createStatisticResponseDto();
    private AccountStatisticResponseDto accountResponse = createAccountStatisticResponseDto();
    private List<Post> posts = List.of(createPost(TEST_ID), createPost(TEST_SECOND_ID));
    private List<Account> accounts = List.of(createTestAccount(TEST_ID), createTestAccount(TEST_SECOND_ID));
    private StatisticRequestDto request = createStatisticRequestDto();

    @BeforeEach
    public void beforeMethod(){
       statisticService = new StatisticService(postRepository, commentRepository, likeRepository, accountRepository, adminConsoleMapper);
    }

    @Test
    void funPostLikeCommentStatisticInvokePostRepo() {
        statisticService.postLikeCommentStatistic(request, StatisticType.POST);
        verify(postRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void funPostLikeCommentStatisticInvokeLikeRepo() {
        statisticService.postLikeCommentStatistic(request, StatisticType.LIKE);
        verify(likeRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void funPostLikeCommentStatisticInvokeCommentRepo() {
        statisticService.postLikeCommentStatistic(request, StatisticType.COMMENT);
        verify(commentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void funPostLikeCommentStatisticInvokeAdminConsoleMapper() {
        statisticService.postLikeCommentStatistic(request, StatisticType.POST);
        verify(adminConsoleMapper, times(1)).dataToStatisticResponse(any(ZonedDateTime.class), anyInt(),any(), any());
    }

    @Test
    void funAccountStatisticInvokeAdminConsoleMapper() {
        statisticService.postLikeCommentStatistic(request, StatisticType.POST);
        verify(adminConsoleMapper, times(1)).dataToStatisticResponse(any(ZonedDateTime.class), anyInt(),any(), any());
    }

    @Test
    void funPostLikeCommentStatisticShouldReturnStatisticResponseDto() {
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(adminConsoleMapper.dataToStatisticResponse(any(ZonedDateTime.class), anyInt(),any(), any())).thenReturn(response);
        StatisticResponseDto response = statisticService.postLikeCommentStatistic(request, StatisticType.POST);
        assertNotNull(response);
    }

}