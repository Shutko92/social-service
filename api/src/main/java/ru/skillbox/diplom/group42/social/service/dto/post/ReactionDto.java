package ru.skillbox.diplom.group42.social.service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Данные реакций")
public class ReactionDto {
    @Schema(description = "Тип реакции")
    private String reactionType;
    @Schema(description = "Количество реакций")
    private Integer count;
}
