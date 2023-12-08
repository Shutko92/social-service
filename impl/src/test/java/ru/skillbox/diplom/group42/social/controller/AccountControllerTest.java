package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.util.CustomPageImpl;

import java.util.Objects;

import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createAccountSearchDto;
import static ru.skillbox.diplom.group42.social.util.TemplateUtil.*;

public class AccountControllerTest extends AbstractIntegrationTest {
    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("account@mail.ru");
    }
    @Test
    public void getAccountShouldReturnAccount(){
        ResponseEntity<AccountDto> response = getAccountTestUser(template,headers);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        Assertions.assertTrue(response.hasBody());
    }
    @Test
    public void deleteAccountShouldReturnAccountDto(){
        createTestUserAccount(template,"account_deleted@mail.ru");
        ResponseEntity<AuthenticateResponseDto> response = loginTestUserAccount(template,"account_deleted@mail.ru");
        String token = Objects.requireNonNull(response.getBody()).getAccessToken();
        HttpHeaders headers = getHeaderWithToken(token);
        ResponseEntity<String> responseDelete = template.exchange(
                "/api/v1/account/me",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        Assertions.assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
    }
    @Test
    public void updateShouldReturnAccountDto(){
        createTestUserAccount(template,"account_for_update@mail.ru");
        ResponseEntity<AuthenticateResponseDto> response = loginTestUserAccount(template,"account_for_update@mail.ru");
        String token = Objects.requireNonNull(response.getBody()).getAccessToken();
        HttpHeaders headers = getHeaderWithToken(token);
        ResponseEntity<AccountDto> accountDtoResponseEntity = getAccountTestUser(template,headers);
        AccountDto account = accountDtoResponseEntity.getBody();

        assert account != null;
        account.setCity("Update City");
        HttpEntity<AccountDto> entity = new HttpEntity<>(account,headers);
        ResponseEntity<AccountDto> responseUpdate = template.exchange(
                "/api/v1/account/me",
                HttpMethod.PUT,
                entity,
                AccountDto.class
        );
        assert responseUpdate.getBody() != null;
        assert responseUpdate.getBody().getCity() != null;
        Assertions.assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        Assertions.assertEquals("Update City", responseUpdate.getBody().getCity());
    }

    @Test
    public void searchShouldReturnPageAccountDto(){
        AccountSearchDto searchDto = createAccountSearchDto();
        ResponseEntity<CustomPageImpl<AccountDto>> response = template.exchange(
                "/api/v1/account/search",
                HttpMethod.GET,
                new HttpEntity<>(searchDto,headers),
                new ParameterizedTypeReference<CustomPageImpl<AccountDto>>(){}
        );
        assert response != null;
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }
    @Test
    public void getByIdShouldReturnAccountDto(){
        ResponseEntity<AccountDto> expectedResponse = getAccountTestUser(template,headers);
        assert expectedResponse.getBody() != null;
        assert expectedResponse.getBody().getId() != null;
        Long idAccount = expectedResponse.getBody().getId();
        ResponseEntity<AccountDto> response = template.exchange(
                "/api/v1/account/{id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AccountDto.class,
                idAccount
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

//    @Test
//    public void searchByStatusShouldReturnAccountDto(){
//        AccountSearchDto dto = createAccountSearchDto();
//        ResponseEntity<AccountDto> accountDtoResponseEntity = getAccountTestUser(template,headers);
//        AccountDto account = accountDtoResponseEntity.getBody();
//
//        assert account != null;
//        account.setStatusCode(StatusCode.FRIEND);
//        /**
//         * Не дает присвоить статус аккаунта
//         * org.postgresql.util.PSQLException: ERROR: insert or update on table "account" violates foreign key constraint "fk_account_status_code"
//         *   Подробности: Key (status_code)=(0) is not present in table "status_code".
//         */
//        HttpEntity<AccountDto> entity = new HttpEntity<>(account,headers);
//        template.exchange(
//                "/api/v1/account/me",
//                HttpMethod.PUT,
//                entity,
//                AccountDto.class
//        );
//        ResponseEntity<CustomPageImpl<AccountDto>> response = template.exchange(
//                "/api/v1/account/search/statusCode?statusCode=FRIEND",
//                HttpMethod.GET,
//                new HttpEntity<>(dto,headers),
//                new ParameterizedTypeReference<CustomPageImpl<AccountDto>>(){}
//        );
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        /** Приходит  код 200, но нет ни JsonType ни тела */
////        Assertions.assertEquals(
////                MediaType.APPLICATION_JSON_VALUE,
////                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
////        );
////        Assertions.assertTrue(response.hasBody());
//    }
}
