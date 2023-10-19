package ru.skillbox.diplom.group42.social.service.mapper.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.entity.post.Post;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface PostMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "tags", ignore = true)
    PostDto convertToDTO(Post post);

    @Mapping(target = "time", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "publishDate", expression = "java(postDto.getPublishDate() == null ? ZonedDateTime.now() : postDto.getPublishDate())")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "timeChanged", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "authorId", expression = "java(ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    @Mapping(target = "commentsCount", constant = "0")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "likeAmount", constant = "0")
    @Mapping(target = "myLike", constant = "false")
    Post createEntity(PostDto postDto);

}
