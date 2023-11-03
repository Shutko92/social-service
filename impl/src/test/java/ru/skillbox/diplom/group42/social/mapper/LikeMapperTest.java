package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;
import ru.skillbox.diplom.group42.social.service.mapper.like.LikeMapper;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createLike;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createLikeDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;

public class LikeMapperTest {
    private final LikeMapper likeMapper = Mappers.getMapper(LikeMapper.class);

    @Test
    public void funConvertToDtoIsCorrect() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            LikeDto expectedLikeDto = createLikeDto(TEST_ACCOUNT_ID);
            Like like = createLike(TEST_ACCOUNT_ID);
            LikeDto actualLikeDto = likeMapper.convertToDto(like);
            expectedLikeDto.setTime(actualLikeDto.getTime());

            Assertions.assertEquals(expectedLikeDto, actualLikeDto);
        }
    }
}
