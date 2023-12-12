package ru.skillbox.diplom.group42.social.service.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group42.social.service.repository.tag.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Метод сохраняет новые теги по параметрам запроса коллеции TagDto, если они не были найдены в базе данных.
     * @param tagDto название.
     * @return список тегов.
     */
    public Set<Tag> create(Set<TagDto> tagDto) {
        Set<Tag> tagSet = new HashSet<>();
        List<Tag> tagList = tagRepository.findByNameIn(tagMapper.convertSetToEntity(tagDto).stream()
                .map(Tag::getName).collect(Collectors.toList()));
        Set<Tag> convertListTagDto = tagMapper.convertSetToEntity(tagDto);

        convertListTagDto.forEach(tag -> {
            if (!tagList.contains(tag)) {
                tag.setIsDeleted(false);
                tagSet.add(tag);
            }
        });
        tagRepository.saveAll(tagSet);
        tagSet.addAll(tagList);
        return tagSet;
    }

    /**
     * Метод обновляет тег по параметру запроса TagDto.
     * @param tagDto С
     * @return название.
     */
    public TagDto update(TagDto tagDto) {
        return tagMapper.convertToDto(tagRepository.save(tagMapper.convertToEntity(tagDto)));
    }

    /**
     * Метод возвращает тег по параметру запроса TagSearchDto.
     * @param tagSearchDto название.
     * @return название.
     */
    public TagDto getTag(TagSearchDto tagSearchDto) {
        return tagMapper.convertToDto(tagRepository.findByName(tagSearchDto.getName()));
    }

    /**
     * Метод удаляет тег по запрошенному id.
     * @param id идентификатор тега.
     */
    public void deleteById(Long id) {
        tagRepository.deleteById(id);

    }

}
