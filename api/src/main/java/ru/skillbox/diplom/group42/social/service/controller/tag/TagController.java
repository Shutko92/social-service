package ru.skillbox.diplom.group42.social.service.controller.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@RequestMapping(ConstantURL.BASE_URL + "/tag")
@Tag(name = "Тег контроллер", description = "Реализация CRUD для тегов")
public interface TagController {
    @Operation(summary = "Создание тега"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Тег создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = TagDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с тегами не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PostMapping
    ResponseEntity<TagDto> create(@RequestBody @Parameter(name = "TagDto") TagDto tagDto);

    @Operation(summary = "Получение тега"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Тег найден"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = TagDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с тегами не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @GetMapping
    ResponseEntity getTag(@Parameter(name = "TagSearchDto") TagSearchDto tagSearchDto);

    @Operation(summary = "Обновление тега"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Тег обновлен"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = TagDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с тегами не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PutMapping
    ResponseEntity<TagDto> update(@RequestBody @Parameter(name = "TagDto") TagDto tagDto);

    @Operation(summary = "Удаление тега"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Тег удален"),
            @ApiResponse(responseCode = "404", description = "Страница с тегами не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable @Parameter(name = "tagId") Long id);

}
