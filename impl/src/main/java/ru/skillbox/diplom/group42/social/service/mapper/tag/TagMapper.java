package ru.skillbox.diplom.group42.social.service.mapper.tag;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag convertToEntity(TagDto tagDto);

    TagDto convertToDto(Tag entity);

    @Mapping(target = "tags", ignore = true)
    Set<TagDto> convertSetToDto(Set<Tag> entity);

    @Mapping(target = "tags", ignore = true)
    Set<Tag> convertSetToEntity(Set<TagDto> tagDto);

}
