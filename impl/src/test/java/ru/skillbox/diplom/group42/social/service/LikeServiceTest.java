package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.ReactionType;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.mapper.like.LikeMapper;
import ru.skillbox.diplom.group42.social.service.repository.post.CommentRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.LikeRepository;
import ru.skillbox.diplom.group42.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group42.social.service.service.like.LikeService;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private LikeMapper likeMapper;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    private LikeService likeService;

    @BeforeEach
    public void beforeMethod() {
        likeService = new LikeService(likeRepository, likeMapper, postRepository, commentRepository);
    }

    @Test
    public void funDeleteLikeShouldLikeInvokeRepositoryFunctions() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            Like like = createLike(TEST_ID);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(
                    anyLong(),
                    anyLong(),
                    any(TypeLike.class)
            )).thenReturn(Optional.of(like));
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(createComment(TEST_ID)));
            likeService.deleteLike(TEST_ID, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
            verify(likeRepository).deleteById(anyLong());
        }
    }

    @Test
    public void funDeleteLikeShouldLikeNotInvokeRepositoryFunctionDeleteByIdWhenLikeIsDeleted() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            Like like = createLike(TEST_ID);
            /** Требует и ID null и IsDeteted true, а должно вроде одного хватать*/
            like.setIsDeleted(true);
            like.setId(null);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(
                    anyLong(),
                    anyLong(),
                    any(TypeLike.class)
            )).thenReturn(Optional.of(like));
            likeService.deleteLike(TEST_ID, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
            verify(likeRepository, never()).deleteById(anyLong());
        }
    }

    @Test
    public void funAddLikeShouldInvokeLikeRepositoryFunctions() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            LikeDto likeDto = createLikeDto(TEST_ID);
            Like like = createLike(TEST_ID);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(
                    anyLong(),
                    anyLong(),
                    any(TypeLike.class)
            )).thenReturn(Optional.of(like));
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(createComment(TEST_ID)));
            likeService.addLike(TEST_ID, likeDto, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
            verify(likeRepository).save(any(Like.class));
        }
    }

    @Test
    public void funAddLikeShouldInvokeLikeRepositoryFunctionsWhenTypeLikeDtoIsPOST() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            LikeDto likeDto = createLikeDto(TEST_ID);
            Like like = createLike(TEST_ID);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(
                    anyLong(),
                    anyLong(),
                    any(TypeLike.class)
            )).thenReturn(Optional.of(like));

            when(postRepository.findById(anyLong())).thenReturn(Optional.of(createPost(TEST_ID)));
            likeService.addLike(TEST_ID, likeDto, TypeLike.POST);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
            verify(likeRepository).save(any(Like.class));
            verify(postRepository).findById(anyLong());
            verify(postRepository).save(any(Post.class));
        }
    }

    @Test
    public void funAddLikeShouldInvokeLikeRepositoryFunctionsWhenLikeIsDeleted() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            LikeDto likeDto = createLikeDto(TEST_ID);
            Like like = createLike(TEST_ID);
            like.setIsDeleted(true);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(
                    anyLong(),
                    anyLong(),
                    any(TypeLike.class)
            )).thenReturn(Optional.of(like));
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(createComment(TEST_ID)));
            likeService.addLike(TEST_ID, likeDto, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
            verify(likeRepository).save(any(Like.class));
        }
    }

    @Test
    public void funAddLikeShouldInvokeLikeRepositoryFunctionsWhenLikeIdNull() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            LikeDto likeDto = createLikeDto(TEST_ID);
            Like like = createLike(TEST_ID);
            like.setId(null);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(
                    anyLong(),
                    anyLong(),
                    any(TypeLike.class)
            )).thenReturn(Optional.of(like));
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(createComment(TEST_ID)));
            when(likeMapper.createEntity(any(LikeDto.class))).thenReturn(like);
            likeService.addLike(TEST_ID, likeDto, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
            verify(likeRepository).save(any(Like.class));
        }
    }

    @Test
    public void funGetSetReactionDtoShouldInvokeLikeRepositoryFunctions() {
        List<Like> likeList = new ArrayList<>();
        likeList.add(createLike(TEST_ID));
        when(likeRepository.findAllByItemIdAndTypeLikeAndIsDeletedFalse(anyLong(), any(TypeLike.class))).thenReturn(likeList);
        likeService.getSetReactionDto(TEST_ID);
        verify(likeRepository).findAllByItemIdAndTypeLikeAndIsDeletedFalse(anyLong(), any(TypeLike.class));
        verify(likeRepository).countByItemIdAndReactionType(anyLong(), any(ReactionType.class));
    }

    @Test
    public void funGetMyReactionShouldInvokeLikeRepositoryFunction() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            when(likeRepository.findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class)))
                    .thenReturn(Optional.of(createLike(TEST_ID)));
            likeService.getMyReaction(TEST_ID, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
        }
    }

    @Test
    public void funIsThereMyLikeToCommentShouldInvokeLikeRepositoryFunction() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            likeService.isThereMyLikeToComment(TEST_ID, TypeLike.COMMENT);
            verify(likeRepository).findByAuthorIdAndItemIdAndTypeLike(anyLong(), anyLong(), any(TypeLike.class));
        }
    }

}
