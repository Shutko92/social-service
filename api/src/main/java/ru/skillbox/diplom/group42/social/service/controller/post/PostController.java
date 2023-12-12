package ru.skillbox.diplom.group42.social.service.controller.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.controller.base.BaseController;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@RequestMapping(ConstantURL.BASE_URL + "/post")
@Tag(name = "Контроллер для постов, комментариев, лайков"
        , description = "Реализация CRUD для постов, комментариев и лайков, также получение статистики")
public interface PostController extends BaseController<PostDto, PostSearchDto> {

    @Operation(summary = "Получение поста по id"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост найден"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Пост не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @Override
    @GetMapping(value = "/{id}")
    ResponseEntity<PostDto> getById(@PathVariable @Parameter(description = "id поста") Long id);

    @Operation(summary = "Получение всех постов"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Посты найден"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(allOf = {Pageable.class, PostDto.class}))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @Override
    @GetMapping
    ResponseEntity<Page<PostDto>> getAll(@Parameter(description = "ДТО поиска") PostSearchDto postSearchDTO, @Parameter(description = "параметры пагинации") Pageable pageable);

    @Operation(summary = "Создание поста"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @Override
    @PostMapping
    ResponseEntity<PostDto> create(@RequestBody @Parameter(description = "ДТО поста") PostDto dto);

    @Operation(summary = "Обновление поста"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост обновлен"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @Override
    @PutMapping
    ResponseEntity<PostDto> update(@RequestBody @Parameter(description = "ДТО поста") PostDto dto);

    @Operation(summary = "Удаление поста по id"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост удален"),
            @ApiResponse(responseCode = "404", description = "Пост не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @Override
    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable @Parameter(description = "id поста") Long id);

    //-------------------------=Comment=-------------------------//
    @Operation(summary = "Получение всех комментариев к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарии найдены"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(allOf = {CommentDto.class, Pageable.class}))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @GetMapping("/{postId}/comment")
    ResponseEntity<Page<CommentDto>> getAllCommentsToPost(@PathVariable @Parameter(description = "postId") Long postId
            , @Parameter(description = "CommentSearchDto") CommentSearchDto commentSearchDto
            , @Parameter(description = "Pageable") Pageable pageable);

    @Operation(summary = "Получение всех сабкомментариев к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Сабкомментарии найдены"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(allOf = {CommentDto.class, Pageable.class}))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @GetMapping("/{postId}/comment/{commentId}/subcomment")
    ResponseEntity<Page<CommentDto>> getSubcomment(@PathVariable @Parameter(description = "postId") Long postId
            , @PathVariable @Parameter(description = "commentId") Long commentId
            , @Parameter(description = "CommentSearchDto") CommentSearchDto commentSearchDto
            , @Parameter(description = "Pageable") Pageable pageable);

    @Operation(summary = "Создание комментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарий создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @PostMapping("/{postId}/comment")
    ResponseEntity<CommentDto> create(@PathVariable @Parameter(description = "postId") Long postId
            , @Parameter(description = "CommentDto") @RequestBody CommentDto dto);

    @Operation(summary = "Создание сабкомментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Сабкомментарий создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @PutMapping("/{id}/comment/{commentId}")
    ResponseEntity<CommentDto> createSubComment(@PathVariable("id") @Parameter(description = "postId") Long id
            , @PathVariable("commentId") @Parameter(description = "commentId") Long commentId
            , @RequestBody @Parameter(description = "CommentDto") CommentDto dto);

    @Operation(summary = "Обновление комментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарий обновлен"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @PutMapping("/{id}/comment")
    ResponseEntity<CommentDto> update(@PathVariable("id") @Parameter(description = "commentId") Long id
            , @RequestBody @Parameter(description = "CommentDto") CommentDto dto);

    @Operation(summary = "Удаление комментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарий удален"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @DeleteMapping("/{id}/comment/{commentId}")
    ResponseEntity deleteById(@PathVariable("id") @Parameter(description = "postId") Long id,
                              @PathVariable("commentId") @Parameter(description = "commentId") Long commentId);

    //-------------------------=Reaction=-------------------------//
    @Operation(summary = "Создание реакции к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Реакция создана"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = LikeDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @PostMapping("/{id}/like")
    ResponseEntity<LikeDto> createPostReaction(@PathVariable @Parameter(description = "postId") Long id
            , @RequestBody LikeDto likeDto);

    @Operation(summary = "Удаление реакции к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Реакция удалена"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @DeleteMapping("/{id}/like")
    ResponseEntity deletePostReaction(@PathVariable @Parameter(description = "postId") Long id);

    //-------------------------=Like=-------------------------//
    @Operation(summary = "Создание лайка к комментарию"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Лайк создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = LikeDto.class))),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})

    @PostMapping("/{id}/comment/{commentId}/like")
    ResponseEntity<LikeDto> createCommentLike(@PathVariable @Parameter(description = "postId") Long id, @Parameter(description = "likeDto") LikeDto likeDto
            , @PathVariable @Parameter(description = "commentId") Long commentId);

    @Operation(summary = "Удаление лайка к комментарию"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Лайк удален"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"
                    , content = @Content()),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content()),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"
                    , content = @Content)})
    @DeleteMapping("/{id}/comment/{commentId}/like")
    ResponseEntity deleteCommentLike(@PathVariable @Parameter(description = "postId") Long id
            , @PathVariable @Parameter(description = "commentId") Long commentId);

}
