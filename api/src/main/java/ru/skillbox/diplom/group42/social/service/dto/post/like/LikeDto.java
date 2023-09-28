package ru.skillbox.diplom.group42.social.service.dto.post.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

import java.time.ZonedDateTime;

@Getter
@Setter
@Schema(description = "Данные лайка")
@EqualsAndHashCode
public class LikeDto extends BaseDto {
    @Schema(description = "Id автора лайка")
    private Long authorId;
    @Schema(description = "Время создания лайка")
    private ZonedDateTime time;
    @Schema(description = "Id сущности, которой проставлен лайк (пост/комментарий)")
    private Long itemId;
    @Schema(description = "Тип лайка")
    private TypeLike type;
    @Schema(description = "Тип реакции")
    private String reactionType;

}
