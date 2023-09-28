package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;
import ru.skillbox.diplom.group42.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createComment;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createCommentDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;

public class CommentMapperTest {
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    public void funCreateEntityIsCorrect() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            Comment expectedComment = createComment(TEST_ACCOUNT_ID);
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            CommentDto commentDto = createCommentDto(TEST_ACCOUNT_ID);
            Comment actualComment = commentMapper.createEntity(commentDto);
            expectedComment.setTime(actualComment.getTime());
            expectedComment.setTimeChanged(actualComment.getTimeChanged());
            expectedComment.setLikeAmount(0);

            Assertions.assertEquals(expectedComment, actualComment);
        }
    }
    @Test
    public void funConvertToDtoIsCorrect() {
        CommentDto expectedDto = createCommentDto(TEST_ACCOUNT_ID);
        Comment comment = createComment(TEST_ACCOUNT_ID);
        CommentDto actualDto = commentMapper.convertToDto(comment);
        Assertions.assertEquals(expectedDto, actualDto);
    }
}

