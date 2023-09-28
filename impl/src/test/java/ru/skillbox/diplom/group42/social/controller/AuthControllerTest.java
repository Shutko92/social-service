package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.captcha.CaptchaDto;

import java.util.Objects;

import static ru.skillbox.diplom.group42.social.util.ControllerTestingDataFactory.createAuthenticateDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_PASSWORD;

public class AuthControllerTest extends AbstractIntegrationTest {
    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("auth@mail.ru");
    }
    @Test
    public void loginShouldReturnCode200WhenSuccess() {
        ResponseEntity<AuthenticateResponseDto> response = template.postForEntity(
                "/api/v1/auth/login",
                new HttpEntity<>(createAuthenticateDto("auth@mail.ru", TEST_PASSWORD)),
                AuthenticateResponseDto.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        Assertions.assertTrue(response.hasBody());
    }
    @Test
    public void logoutShouldReturnCode200(){
        ResponseEntity<HttpStatus> response = template.postForEntity(
                "/api/v1/auth/logout",
                null,
                HttpStatus.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void getCaptchaImageShouldReturnCaptchaDto(){
        ResponseEntity<CaptchaDto> response = template.getForEntity(
                "/api/v1/auth/captcha",
                null,
                CaptchaDto.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }
}
