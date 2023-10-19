package ru.skillbox.diplom.group42.social.service.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.entity.post.comment.Comment;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface CommentMapper {

    @Mapping(target = "time", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "timeChanged", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "authorId", expression = "java(ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "commentCount", expression = "java(commentDto.getCommentCount() == null ? 0 : commentDto.getCommentCount())")
    @Mapping(target = "isDeleted" , constant = "false")
    @Mapping(target = "likeAmount", constant = "0")
    Comment createEntity(CommentDto commentDto);

    CommentDto convertToDto(Comment comment);

    Comment convertToEntity(CommentDto commentDto);


}
