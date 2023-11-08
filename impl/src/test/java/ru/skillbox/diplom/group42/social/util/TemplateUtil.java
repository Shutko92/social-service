package ru.skillbox.diplom.group42.social.util;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;

import java.util.List;
import java.util.Set;

import static ru.skillbox.diplom.group42.social.util.ControllerTestingDataFactory.createAuthenticateDto;
import static ru.skillbox.diplom.group42.social.util.ControllerTestingDataFactory.createRegistrationDtoForTestUser;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createPostDto;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTegDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.*;

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

    public static ResponseEntity<PostDto> createTestPostWithTags(TestRestTemplate template, HttpHeaders headers) {
        PostDto postDto = createPostDto(TEST_ID);
        postDto.setTags(Set.of(createTegDto(6)));
        ResponseEntity<PostDto> response = template.postForEntity("/api/v1/post", new HttpEntity<>(postDto, headers), PostDto.class);
        return response;
    }

    public static DialogDto createDialogDto() {
        DialogDto dialog = new DialogDto();
        dialog.setId(TEST_ID);
        dialog.setLastMessage(List.of());
        dialog.setUnreadCount(0);
        dialog.setConversationPartner1(TEST_ID);
        dialog.setConversationPartner2(TEST_SECOND_ID);
        dialog.setIsDeleted(false);
        return  dialog;
    }
}

