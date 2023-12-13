package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.friend.CountDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createFriendSearchDto;
import static ru.skillbox.diplom.group42.social.util.TemplateUtil.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendControllerTest extends AbstractIntegrationTest{
    private static Long friend1;
    private static Long friend2;

    public static Long getFriend1() {
        return friend1;
    }

    public static void setFriend1(Long friend1) {
        FriendControllerTest.friend1 = friend1;
    }

    public static Long getFriend2() {
        return friend2;
    }

    public static void setFriend2(Long friend2) {
        FriendControllerTest.friend2 = friend2;
    }

    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("friends@mail.ru");
    }

    @Test
    @Order(1)
    void countFriendRequestsReturnsCountDto() {
        setFriend1(getAccountTestUser(template,headers).getBody().getId());
        createTestUserAccount(template,"make_friends@mail.ru");
        String token = loginTestUserAccount(template,"make_friends@mail.ru").getBody().getAccessToken();
        HttpHeaders headers = getHeaderWithToken(token);
        ResponseEntity<CountDto> response = template.exchange(
                "/api/v1/friends/count", HttpMethod.GET, new HttpEntity<>(headers), CountDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }

    @Test
    @Order(2)
    void subscribeReturnsFriendShortDto() {
        ResponseEntity<FriendShortDto> response = template.exchange(
                "/api/v1/friends/subscribe/{id}", HttpMethod.POST, new HttpEntity<>(headers), FriendShortDto.class, getFriend1());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }

    @Test
    @Order(3)
    void recommendations() {
        FriendSearchDto friendSearchDto = createFriendSearchDto();
        friendSearchDto.setIdFrom(getFriend1());
        friendSearchDto.setIdTo(getFriend2());
        ResponseEntity<List> response = template.exchange(
                "/api/v1/friends/recommendations", HttpMethod.GET, new HttpEntity<>(friendSearchDto, headers), List.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }
}