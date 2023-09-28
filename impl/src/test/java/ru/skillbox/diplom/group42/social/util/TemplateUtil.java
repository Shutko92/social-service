package ru.skillbox.diplom.group42.social.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;

import static ru.skillbox.diplom.group42.social.util.ControllerTestingDataFactory.createAuthenticateDto;
import static ru.skillbox.diplom.group42.social.util.ControllerTestingDataFactory.createRegistrationDtoForTestUser;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_PASSWORD;

public class TemplateUtil {
    public static HttpStatus createTestUserAccount(TestRestTemplate template, String email){
        ResponseEntity<HttpStatus> status = template.postForEntity(
                "/api/v1/auth/register",
                new HttpEntity<>(createRegistrationDtoForTestUser(email)),
                HttpStatus.class
        );
        return status.getStatusCode();
    }
    public static ResponseEntity<AuthenticateResponseDto> loginTestUserAccount(TestRestTemplate template, String email){
        ResponseEntity<AuthenticateResponseDto> response = template.postForEntity(
                "/api/v1/auth/login",
                new HttpEntity<>(createAuthenticateDto(email, TEST_PASSWORD)),
                AuthenticateResponseDto.class
        );
        return response;
    }
    public static HttpHeaders getHeaderWithToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Cookie", "jwt="+ token);
        return headers;
    }
    public static ResponseEntity<AccountDto> getAccountTestUser(TestRestTemplate template, HttpHeaders headers){
        return template.exchange(
                "/api/v1/account/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AccountDto.class
        );
    }
}

