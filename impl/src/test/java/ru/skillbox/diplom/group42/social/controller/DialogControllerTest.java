package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageShortDto;
import ru.skillbox.diplom.group42.social.service.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;
import ru.skillbox.diplom.group42.social.service.repository.dialog.DialogRepository;
import ru.skillbox.diplom.group42.social.service.repository.message.MessageRepository;
import ru.skillbox.diplom.group42.social.util.CustomPageImpl;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createTestDialog;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createTestMessage;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DialogControllerTest extends AbstractIntegrationTest{
    private static DialogRepository dialogRepository;
    private static MessageRepository messageRepository;


    @BeforeAll
    public static void initEmail(@Autowired DialogRepository repository1, @Autowired MessageRepository repository2) {
        setEmailForCreateAccount("dialog@mail.ru");
        dialogRepository=repository1;
        messageRepository=repository2;
    }

    @Test
    @Order(2)
    void getAllDialogsReturnsDialogDto() {
        ResponseEntity<CustomPageImpl<DialogDto>> response = template.exchange("/api/v1/dialogs", HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }

    @Test
    @Order(3)
    void getCountUnreadMessageReturnsUnreadCountDto() {
        ResponseEntity<UnreadCountDto> response = template.exchange("/api/v1/dialogs/unread", HttpMethod.GET,
                new HttpEntity<>(headers), UnreadCountDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }

    @Test
    @Order(1)
    void getDialogBetweenUsersReturnsDialogDto() {
        ResponseEntity<DialogDto> response = template.exchange("/api/v1/dialogs/recipientId/{id}", HttpMethod.GET,
                new HttpEntity<>(headers), DialogDto.class, TEST_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        Dialog testDialog = createTestDialog();
        testDialog.setConversationPartner1(response.getBody().getConversationPartner1());
        Message testMessage = createTestMessage(dialogRepository.save(testDialog));
        testMessage.setConversationPartner1(response.getBody().getConversationPartner1());
        messageRepository.save(testMessage);
    }

    @Test
    void getMessagesToDialogReturnsPageMessageShortDto() {
        ResponseEntity<CustomPageImpl<MessageShortDto>> response = template.exchange("/api/v1/dialogs/messages?recipientId=1048", HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
    }
}