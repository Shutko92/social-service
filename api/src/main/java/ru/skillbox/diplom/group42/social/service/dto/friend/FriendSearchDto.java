package ru.skillbox.diplom.group42.social.service.dto.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto для поиска записей дружбы по условиям", required = true)
public class FriendSearchDto extends BaseSearchDto {

    @Schema(description = "Идентификатор пользователя, имеющего отношения с текущим пользователем")
    private Long idFrom;

    @Schema(description = "Статус текущих отношений пользователя")
    private String statusCode;

    @Schema(description = "Идентификатор текущего пользователя")
    private Long idTo;

    @Schema(description = "Статус предшествующих отношений пользователя")
    private String previousStatusCode;


}
