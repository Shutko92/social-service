package ru.skillbox.diplom.group42.social.service.controller.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group42.social.service.service.tag.TagService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TagControllerImpl implements TagController {

    private final TagService tagService;

    @Override
    public ResponseEntity<TagDto> create(TagDto tagDto) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<TagDto> getTag(TagSearchDto tagSearchDto) {
        return ResponseEntity.ok(tagService.getTag(tagSearchDto));
    }

    @Override
    public ResponseEntity<TagDto> update(TagDto tagDto) {
        return ResponseEntity.ok(tagService.update(tagDto));
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        tagService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
