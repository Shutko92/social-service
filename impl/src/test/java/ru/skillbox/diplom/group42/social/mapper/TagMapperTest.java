package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;


public class TagMapperTest {
    private static final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
    private static final Tag testTag = createTeg(1);
    private static final TagDto testTagDto = createTegDto(1);
    private static final List<Tag> tagList = createListTag();
    private static final List<TagDto> tagDtoList = createListTagDto();

    @Test
    public void funConvertToEntityIsCorrect(){
        Tag actual = tagMapper.convertToEntity(testTagDto);
        Assertions.assertEquals(testTag,actual);
    }
    @Test
    public void funConvertToDtoIsCorrect(){
        TagDto actual = tagMapper.convertToDto(testTag);
        Assertions.assertEquals(testTagDto,actual);
    }
    @Test
    public void funConvertSetToDtoIsCorrect(){
        Set<TagDto> expected = new HashSet<>(tagDtoList);
        Set<TagDto> actual = tagMapper.convertSetToDto(new HashSet<>(tagList));
        Assertions.assertTrue(expected.equals(actual));
    }
    @Test
    public void funConvertSetToEntityIsCorrect(){
        Set<Tag> expected = new HashSet<>(tagList);
        Set<Tag> actual = tagMapper.convertSetToEntity(new HashSet<>(tagDtoList));
        Assertions.assertTrue(expected.equals(actual));
    }
}