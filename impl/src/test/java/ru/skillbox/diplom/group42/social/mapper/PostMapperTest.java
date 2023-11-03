package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;
import ru.skillbox.diplom.group42.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createPost;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createPostDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;
public class PostMapperTest {
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    @Test
    public void funConvertToDTOIsCorrect() {
        Post post = createPost(TEST_ACCOUNT_ID);
        PostDto expectedPostDto = createPostDto(TEST_ACCOUNT_ID);
        PostDto actualPostDto = postMapper.convertToDTO(post);
        Assertions.assertEquals(expectedPostDto, actualPostDto);
    }

    @Test
    public void funCreateEntityIsCorrect() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            PostDto postDto = createPostDto(TEST_ACCOUNT_ID);
            Post expectedPost = createPost(TEST_ACCOUNT_ID);
            Post actualPost = postMapper.createEntity(postDto);
            expectedPost.setTime(actualPost.getTime());
            expectedPost.setType(actualPost.getType());
            expectedPost.setIsDeleted(actualPost.getIsDeleted());
            expectedPost.setTimeChanged(actualPost.getTimeChanged());
            expectedPost.setCommentsCount(0);
            expectedPost.setLikeAmount(0);
            expectedPost.setMyLike(false);
            Assertions.assertEquals(expectedPost, actualPost);
        }
    }
}
