package ru.skillbox.diplom.group42.social.service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountOnlineDto {

    private Long id;
    private ZonedDateTime lastOnlineTime;
    private Boolean isOnline;

}