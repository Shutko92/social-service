package ru.skillbox.diplom.group42.social.service.dto.friend;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group42.social.service.dto.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto получения параметров дружбы", required = true)
public class FriendShortDto extends BaseDto {

    @Schema(description = "Статус текущих отношений пользователя")
    private StatusCode statusCode;

    @Schema(description = "Идентификатор пользователя-партнера")
    private Long friendId;

    @Schema(description = "Статус предшествующих отношений пользователя")
    private StatusCode previousStatusCode;

    @Schema(description = "Рейтинг пользователя, рекомендуемого в друзья")
    private Integer rating;









}
