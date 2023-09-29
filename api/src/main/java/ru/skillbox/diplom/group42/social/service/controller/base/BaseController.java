package ru.skillbox.diplom.group42.social.service.controller.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;

public interface BaseController<baseDto extends BaseDto, baseSearchDTO extends BaseSearchDto> {

    @GetMapping(value = "{id}")
    ResponseEntity<baseDto> getById(@PathVariable("id") Long id);

    @GetMapping
    ResponseEntity<Page<baseDto>> getAll(baseSearchDTO baseSearchDTO, Pageable pageable);

    @PostMapping
    ResponseEntity<baseDto> create(@RequestBody baseDto dto);

    @PutMapping
    ResponseEntity<baseDto> update(@RequestBody baseDto dto);

    @DeleteMapping("{id}")
    ResponseEntity deleteById(@PathVariable("id") Long id);


}
