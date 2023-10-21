package ru.skillbox.diplom.group42.social.service.mapper.admin.console;

import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountCountPerAgeDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticPerDateDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class AdminConsoleMapper {
    public StatisticResponseDto dataToStatisticResponse(
            ZonedDateTime date, int size, List<StatisticPerDateDto> monthly, List<StatisticPerDateDto> perHour) {

        return new StatisticResponseDto(
                date, size,
                perHour, monthly);
    }

    public AccountStatisticResponseDto dataToAccountStatisticResponse(
            ZonedDateTime date, int size, List<StatisticPerDateDto> monthly,  List<AccountCountPerAgeDto> perAge) {

        return new AccountStatisticResponseDto(
                date, size,
                perAge, monthly);
    }
}
