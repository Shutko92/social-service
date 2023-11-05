package ru.skillbox.diplom.group42.social.service.dto.admin.console.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatisticResponseDto {
    private ZonedDateTime date;
    private int count;
    private List<AccountCountPerAgeDto> countPerAge;
    private List<StatisticPerDateDto> countPerMonth;
}
