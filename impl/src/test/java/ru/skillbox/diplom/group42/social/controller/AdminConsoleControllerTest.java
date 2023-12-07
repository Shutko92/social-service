package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.request.StatisticRequestDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.AccountStatisticResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.admin.console.response.StatisticResponseDto;

import java.util.Objects;

class AdminConsoleControllerTest extends AbstractIntegrationTest {

    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("statistic@mail.ru");
    }

    @Test
    void returnPostShouldReturnStatisticResponse() {
        ResponseEntity<StatisticResponseDto> response = template.exchange(
                "/api/v1/admin-console/statistic/post?date=2023-10-26T00:00:00.735Z&firstMonth=2023-01-01T00:00:00.735Z&lastMonth=2023-12-31T00:00:00.735Z",
                HttpMethod.GET, new HttpEntity<>(StatisticRequestDto.class, headers), StatisticResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString());
        Assertions.assertTrue(response.hasBody());
    }

    @Test
    void returnLikeShouldReturnStatisticResponse() {
        ResponseEntity<StatisticResponseDto> response = template.exchange(
                "/api/v1/admin-console/statistic/like?date=2023-10-26T00:00:00.735Z&firstMonth=2023-01-01T00:00:00.735Z&lastMonth=2023-12-31T00:00:00.735Z",
                HttpMethod.GET, new HttpEntity<>(StatisticRequestDto.class, headers), StatisticResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString());
        Assertions.assertTrue(response.hasBody());
    }

    @Test
    void returnCommentShouldReturnStatisticResponse() {
        ResponseEntity<StatisticResponseDto> response = template.exchange(
                "/api/v1/admin-console/statistic/comment?date=2023-10-26T00:00:00.735Z&firstMonth=2023-01-01T00:00:00.735Z&lastMonth=2023-12-31T00:00:00.735Z",
                HttpMethod.GET, new HttpEntity<>(StatisticRequestDto.class, headers), StatisticResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString());
        Assertions.assertTrue(response.hasBody());
    }

    @Test
    void returnAccountShouldReturnAccountStatisticResponse() {
        ResponseEntity<AccountStatisticResponseDto> response = template.exchange(
                "/api/v1/admin-console/statistic/account?date=2023-10-26T00:00:00.735Z&firstMonth=2023-01-01T00:00:00.735Z&lastMonth=2023-12-31T00:00:00.735Z",
                HttpMethod.GET, new HttpEntity<>(StatisticRequestDto.class, headers), AccountStatisticResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString());
        Assertions.assertTrue(response.hasBody());
    }
}