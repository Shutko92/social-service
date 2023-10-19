package ru.skillbox.diplom.group42.social.service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Данные запроса поста")
public class PostSearchDto extends BaseSearchDto {
     @Schema(description = "id постов")
     private List<Long> ids;
     @Schema(description = "id авторов")
     private List<Long> accountIds;
     @Schema(description = "Список заблокированных id авторов")
     private List<Long> blockedIds;
     @Schema(description = "Автор")
     private String author;
     @Schema(description = "Относится ли автор к друзьям")
     private Boolean withFriends;
     @Schema(description = "Список дегов")
     private List<String> tags;
     private List<String> reaction;
     @Schema(description = "Дата от")
     private ZonedDateTime dateFrom;
     @Schema(description = "Дата до")
     private ZonedDateTime dateTo;
     @Schema(description = "Данные запроса поиска по тексту")
     private String text;

}
