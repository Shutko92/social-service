package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group42.social.service.repository.tag.TagRepository;
import ru.skillbox.diplom.group42.social.service.service.tag.TagService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private TagSearchDto tagSearchDto;
    private TagService tagService;
    @BeforeEach
    public void beforeMethod(){
        tagService = new TagService(tagRepository, tagMapper);
    }
    @Test
    public void funCreateShouldInvokeTagRepositoryFunctions(){
        Set<TagDto> dtoSet = createSetTagDto();
        java.util.List<Tag> tagList =  createListTag();
        when(tagRepository.findByNameIn(anyList())).thenReturn(tagList);
        tagService.create(dtoSet);
        verify(tagRepository).findByNameIn(anyList());
        verify(tagRepository).saveAll(anySet());
    }
    @Test
    public void updateTest(){
        when(tagMapper.convertToEntity(any(TagDto.class))).thenReturn(createTag());
        tagService.update(createTagDto());
        verify(tagRepository).save(any(Tag.class));
    }
    @Test
    public void getAllTest(){
        when(tagSearchDto.getName()).thenReturn("test name");
        tagService.getTag(tagSearchDto);
        verify(tagRepository).findByName(anyString());
    }
    @Test
    public void deleteByIdTest(){
        tagService.deleteById(TEST_ID);
        verify(tagRepository).deleteById(anyLong());
    }
}
