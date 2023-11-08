package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountCountPerAgeDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticPerDateDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.mapper.admin.console.AdminConsoleMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createAccountCountPerAgeDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createStatisticPerDateDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_SIZE;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TIME_TEST;

class AdminConsoleMapperTest {
    private AdminConsoleMapper adminConsoleMapper = Mappers.getMapper(AdminConsoleMapper.class);

    @Test
    void funDataToStatisticResponseIsCorrect() {
        List<StatisticPerDateDto> perDate = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            perDate.add(createStatisticPerDateDto());
        }
        StatisticResponseDto statisticResponseDto = adminConsoleMapper.dataToStatisticResponse(TIME_TEST, TEST_SIZE, perDate, perDate);
        assertEquals(statisticResponseDto.getDate(), TIME_TEST);
        assertEquals(statisticResponseDto.getCount(), TEST_SIZE);
        assertEquals(statisticResponseDto.getCountPerHours(), perDate);
        assertEquals(statisticResponseDto.getCountPerMonth(), perDate);
    }

    @Test
    void funDataToAccountStatisticResponseIsCorrect() {
        List<StatisticPerDateDto> perDate = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            perDate.add(createStatisticPerDateDto());
        }
        List<AccountCountPerAgeDto> perAge = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            perAge.add(createAccountCountPerAgeDto());
        }
        AccountStatisticResponseDto accountStatisticResponseDto = adminConsoleMapper.dataToAccountStatisticResponse(TIME_TEST, TEST_SIZE, perDate, perAge);
        assertEquals(accountStatisticResponseDto.getDate(), TIME_TEST);
        assertEquals(accountStatisticResponseDto.getCount(), TEST_SIZE);
        assertEquals(accountStatisticResponseDto.getCountPerMonth(), perDate);
        assertEquals(accountStatisticResponseDto.getCountPerAge(), perAge);

    }
}