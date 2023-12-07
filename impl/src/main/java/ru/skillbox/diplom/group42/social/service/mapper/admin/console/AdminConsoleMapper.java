package ru.skillbox.diplom.group42.social.service.mapper.admin.console;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountCountPerAgeDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticPerDateDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminConsoleMapper {

    @Mapping(source = "date", target = "date")
    @Mapping(source = "size", target = "count")
    @Mapping(source = "perHour", target = "countPerHours")
    @Mapping(source = "monthly", target = "countPerMonth")
    StatisticResponseDto dataToStatisticResponse(ZonedDateTime date, int size, List<StatisticPerDateDto> monthly, List<StatisticPerDateDto> perHour);

    @Mapping(source = "date", target = "date")
    @Mapping(source = "size", target = "count")
    @Mapping(source = "monthly", target = "countPerMonth")
    @Mapping(source = "perAge", target = "countPerAge")
    AccountStatisticResponseDto dataToAccountStatisticResponse(ZonedDateTime date, int size, List<StatisticPerDateDto> monthly,  List<AccountCountPerAgeDto> perAge);
}
