package ru.skillbox.diplom.group42.social.service.mapper.like;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.entity.post.like.Like;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface LikeMapper {

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "authorId", expression = "java(ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext())")
    @Mapping(target = "time", expression = "java(ZonedDateTime.now())")
    Like createEntity(LikeDto likeDto);

    LikeDto convertToDto(Like like);


}
