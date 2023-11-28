package ru.skillbox.diplom.group42.social.service.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skillbox.diplom.group42.social.service.dto.auth.UserDto;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Schema(description = "Данные аккаунта")
public class AccountDto extends UserDto {

    @Schema(description = "Номер телефона")
    private String phone;
    @Schema(description = "Фотография")
    private String photo;
    @Schema(description = "Обложка профиля")
    private String profileCover;
    @Schema(description = "О себе")
    private String about;
    @Schema(description = "Город")
    private String city;
    @Schema(description = "Страна")
    private String country;
    @Schema(description = "Статус")
    private StatusCode statusCode;
    @Schema(description = "Дата регистрации")
    private ZonedDateTime regDate;
    @Schema(description = "День рождения")
    private ZonedDateTime birthDate;
    @Schema(description = "Разрешение сообщения")
    private String messagePermission;
    @Schema(description = "Дата последнего пребывания в сети")
    private ZonedDateTime lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    @Schema(description = "Смайлик статуса")
    private String emojiStatus;
    @Schema(description = "Дата создания")
    private ZonedDateTime createdOn;
    @Schema(description = "Дата последнего обновления")
    private ZonedDateTime updatedOn;
    @Schema(description = "Дата удаления временной метки")
    private ZonedDateTime deletionTimestamp;
}
