package ru.skillbox.diplom.group42.social.service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.auth.UserDto;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends UserDto {

    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private StatusCode statusCode;
    private ZonedDateTime regDate;
    private ZonedDateTime birthDate;
    private String messagePermission;
    private ZonedDateTime lastOnlineTime;
    private boolean isOnline;
    private boolean isBlocked;
    private String emojiStatus;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private ZonedDateTime deletionTimestamp;
}
