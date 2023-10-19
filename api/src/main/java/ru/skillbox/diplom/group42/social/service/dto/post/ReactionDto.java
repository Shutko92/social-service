package ru.skillbox.diplom.group42.social.service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.post.like.TypeLike;

import java.util.Objects;

@Getter
@Setter
@Schema(description = "Данные реакций")
public class ReactionDto {
    @Schema(description = "Тип реакции")
    private String reactionType;
    @Schema(description = "Количество реакций")
    private Integer count;
    @Schema(description = "Тип реакции (Post)")
    private TypeLike type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReactionDto that = (ReactionDto) o;
        return Objects.equals(reactionType, that.reactionType) && Objects.equals(count, that.count) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reactionType, count, type);
    }
}
