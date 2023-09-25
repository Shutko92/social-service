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
import ru.skillbox.diplom.group42.social.service.dto.statistic.RequestDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@RequestMapping(ConstantURL.BASE_URL + "/post")
@Tag(name = "Контроллер для постов, комментариев, лайков и статистики"
        , description = "Реализация CRUD для постов, комментариев и лайков, также получение статистики")
public interface PostController extends BaseController<PostDto, PostSearchDto> {

    @Operation(summary = "Получение поста по id"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост найден"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Пост не найден"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @Override
    @GetMapping(value = "/{id}")
    ResponseEntity<PostDto> getById(@PathVariable @Parameter(description = "id поста") Long id);
    @Operation(summary = "Получение всех постов"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Посты найден"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(allOf = {Pageable.class, PostDto.class}))),
            @ApiResponse(responseCode = "404", description = "Посты не найден"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @Override
    @GetMapping
    ResponseEntity<Page<PostDto>> getAll(@Parameter(description = "ДТО поиска")PostSearchDto postSearchDTO,@Parameter(description = "параметры пагинации") Pageable pageable);

    //TODO а что не найдено то?
    @Operation(summary = "Создание поста"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Счастье не найдено :("
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @Override
    @PostMapping
    ResponseEntity<PostDto> create(@RequestBody @Parameter(description = "ДТО поста") PostDto dto);
    @Operation(summary = "Обновление поста"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост обновлен"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Пост не найден"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @Override
    @PutMapping
    ResponseEntity<PostDto> update(@RequestBody @Parameter(description = "ДТО поста") PostDto dto);
    @Operation(summary = "Удаление поста по id"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост удален"),
            @ApiResponse(responseCode = "404", description = "Пост не найден"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос")})
    @Override
    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable @Parameter(description = "id поста") Long id);

    @Operation(summary = "Создание отложенного поста по ДТО"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Пост создан"),
            @ApiResponse(responseCode = "404", description = "Пост не найден"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос")})
    @PutMapping("/delayed")
    ResponseEntity delayedPost(@RequestBody @Parameter(description = "PostDto") PostDto postDto);


    //-------------------------=Comment=-------------------------//
    @Operation(summary = "Получение всех комментариев к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарии найдены"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(allOf = {CommentDto.class, Pageable.class}))),
            @ApiResponse(responseCode = "404", description = "Страница с комментариями не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @GetMapping("/{postId}/comment")
    ResponseEntity<Page<CommentDto>> getAllCommentsToPost(@PathVariable @Parameter(description = "postId") Long postId
            , @Parameter(description = "CommentSearchDto") CommentSearchDto commentSearchDto
            ,@Parameter(description = "Pageable") Pageable pageable);

    @Operation(summary = "Получение всех сабкомментариев к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Сабкомментарии найдены"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(allOf = {CommentDto.class, Pageable.class}))),
            @ApiResponse(responseCode = "404", description = "Страница с сабкомментариями не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
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
            @ApiResponse(responseCode = "404", description = "Страница с комментариями не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PostMapping("/{postId}/comment")
    ResponseEntity<CommentDto> create(@PathVariable @Parameter(description = "postId") Long postId
            ,@Parameter(description = "CommentDto") @RequestBody CommentDto dto);

    @Operation(summary = "Создание сабкомментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Сабкомментарий создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с сабкомментариями не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PutMapping("/{id}/comment/{commentId}")
    ResponseEntity<CommentDto> createSubComment(@PathVariable("id") @Parameter(description = "postId") Long id
            , @PathVariable("commentId") @Parameter(description = "commentId") Long commentId
            , @RequestBody @Parameter(description = "CommentDto") CommentDto dto);

    @Operation(summary = "Обновление комментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарий обновлен"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с комментариями не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PutMapping("/{id}/comment")
    ResponseEntity<CommentDto> update(@PathVariable("id") @Parameter(description = "commentId") Long id
            , @RequestBody @Parameter(description = "CommentDto") CommentDto dto);

    @Operation(summary = "Удаление комментариев"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Комментарий удален"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Страница с комментариями не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @DeleteMapping("/{id}/comment/{commentId}")
    ResponseEntity deleteById(@PathVariable("id") @Parameter(description = "postId") Long id, @PathVariable("commentId") @Parameter(description = "commentId") Long commentId);


    //-------------------------=Like=-------------------------//
    @Operation(summary = "Создание лайка к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Лайк создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = LikeDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с лайками не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PostMapping("/{id}/like")
    ResponseEntity<LikeDto> createPostLike(@PathVariable @Parameter(description = "postId") Long id);
    @Operation(summary = "Удаление лайка к посту"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Лайк удален"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Страница с лайками не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @DeleteMapping("/{id}/like")
    ResponseEntity deletePostLike(@PathVariable @Parameter(description = "postId") Long id);
    @Operation(summary = "Создание лайка к комментарию"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Лайк создан"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                    , schema = @Schema(implementation = LikeDto.class))),
            @ApiResponse(responseCode = "404", description = "Страница с лайками не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @PostMapping("/{id}/comment/{commentId}/like")
    ResponseEntity<LikeDto> createCommentLike(@PathVariable @Parameter(description = "postId") Long id, @PathVariable @Parameter(description = "commentId") Long commentId);
    @Operation(summary = "Удаление лайка к комментарию"
            , responses = {
            @ApiResponse(responseCode = "200", description = "Лайк удален"
                    , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Страница с лайками не найдена"
                    , content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"
                    , content = @Content(mediaType = "", schema = @Schema(nullable = true)))})
    @DeleteMapping("/{id}/comment/{commentId}/like")
    ResponseEntity deleteCommentLike(@PathVariable @Parameter(description = "postId") Long id, @PathVariable @Parameter(description = "commentId") Long commentId);


    //-------------------------=Statistic=-------------------------//


    @GetMapping("statistic/post")
    ResponseEntity<?> getStatisticOfPost(RequestDto requestDto);

    @GetMapping("statistic/comment")
    ResponseEntity<?> getStatisticOfComment(RequestDto requestDto);
}
