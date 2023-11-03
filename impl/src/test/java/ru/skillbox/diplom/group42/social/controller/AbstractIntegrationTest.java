package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.skillbox.diplom.group42.social.config.EmbeddedPostgresConfig;
import ru.skillbox.diplom.group42.social.service.Application;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;

import java.util.Objects;

import static ru.skillbox.diplom.group42.social.util.TemplateUtil.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ContextConfiguration(classes = EmbeddedPostgresConfig.class)
public class AbstractIntegrationTest {

    public static TestRestTemplate template;
    public static HttpHeaders headers;
    public static String userEmail;

    @BeforeAll
    public static void beforeClass(@Autowired TestRestTemplate templateRest) {
        template = templateRest;
    }
    @AfterAll
    public static void afterClass() {template = null;}

    @BeforeEach
    public void beforeMethod() {
        ResponseEntity<AuthenticateResponseDto> response = loginTestUserAccount(template, userEmail);
        String token = Objects.requireNonNull(response.getBody()).getAccessToken();
        headers = getHeaderWithToken(token);
    }
    public static void setEmailForCreateAccount(String email){
        userEmail = email;
        createTestUserAccount(template, userEmail);
    }
}
