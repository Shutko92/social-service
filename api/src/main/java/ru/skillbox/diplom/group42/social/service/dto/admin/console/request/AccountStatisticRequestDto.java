package ru.skillbox.diplom.group42.social.service.dto.admin.console.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatisticRequestDto {
    private ZonedDateTime date;
    private ZonedDateTime firstMonth;
    private ZonedDateTime lastMonth;
}
