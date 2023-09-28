package ru.skillbox.diplom.group42.social.service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Данные поста")
@EqualsAndHashCode
public class PostDto extends BaseDto {

    @Schema(description = "Время создания")
    private ZonedDateTime time;
    @Schema(description = "Время редактирования")
    private ZonedDateTime timeChanged;
    @Schema(description = "Id автора")
    private Long authorId;
    @Schema(description = "Заголовок")
    private String title;
    @Schema(description = "Тип поста (опубликованный/отложенный)")
    private Type type;
    @Schema(description = "Текст поста")
    private String postText;
    @Schema(description = "Заблокирован ли аккаунт автора")
    private Boolean isBlocked;
    @Schema(description = "Количество комментариев")
    private Integer commentsCount;
    @Schema(description = "Список тегов")
    private Set<TagDto> tags;
    @Schema(description = "Список реакций")
    private Set<ReactionDto> reactions;
    @Schema(description = "Моя реакция")
    private String myReaction;
    @Schema(description = "Количество лайков")
    private Integer likeAmount;
    @Schema(description = "Есть ли мой лайк")
    private Boolean myLike;
    @Schema(description = "Путь к картинке")
    private String imagePath;
    @Schema(description = "Дата публикации")
    private ZonedDateTime publishDate;

}
