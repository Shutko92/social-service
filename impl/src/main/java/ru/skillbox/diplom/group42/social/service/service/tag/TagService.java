package ru.skillbox.diplom.group42.social.service.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag;
import ru.skillbox.diplom.group42.social.service.entity.tag.Tag_;
import ru.skillbox.diplom.group42.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group42.social.service.repository.tag.TagRepository;
import ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil;

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

    public Set<Tag> create(Set<TagDto> tagDto) {
        log.info("start TagService  create Tag: " + tagDto.toString());
        Set<Tag> tagSet = new HashSet<>();
        List<Tag> tagList = tagRepository.findByNameIn(tagMapper.convertSetToEntity(tagDto).stream()
                .map(Tag::getName).collect(Collectors.toList()));
        Set<Tag> convertListTagDto = tagMapper.convertSetToEntity(tagDto);

        convertListTagDto.forEach(tag -> {
            if (!tagList.contains(tag)){
                tag.setIsDeleted(false);
                tagSet.add(tag);
            }
        });
        tagRepository.saveAll(tagSet);
        tagSet.addAll(tagList);
        return tagSet;
    }

    public TagDto update(TagDto tagDto) {
        return tagMapper.convertToDto(tagRepository.save(tagMapper.convertToEntity(tagDto)));
    }

    public TagDto getAll(TagSearchDto tagSearchDto) {

        return tagMapper.convertToDto(tagRepository.findByName(tagSearchDto.getName()));

    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);

    }

    private Specification<Tag> getSpecification(TagSearchDto tagSearchDto) {
        return SpecificationUtil.getBaseSpecification(tagSearchDto)
                .and(SpecificationUtil.like(Tag_.name, tagSearchDto.getName(), true));
    }
}
