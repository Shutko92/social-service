package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group42.social.service.dto.dialog.DialogDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.ReadStatus;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;
import ru.skillbox.diplom.group42.social.service.mapper.dialog.DialogMapper;
import ru.skillbox.diplom.group42.social.service.mapper.message.MessageMapper;
import ru.skillbox.diplom.group42.social.service.repository.dialog.DialogRepository;
import ru.skillbox.diplom.group42.social.service.repository.message.MessageRepository;
import ru.skillbox.diplom.group42.social.service.service.dialog.DialogService;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationHandler;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.TemplateUtil.createDialogDto;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.*;

@ExtendWith(MockitoExtension.class)
class DialogServiceTest {
    @Mock
    private DialogRepository dialogRepository;
    @Mock
    private DialogMapper dialogMapper;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private MessageRepository messageRepository;
    private DialogService dialogService;
    @Mock
    private NotificationHandler notificationHandler;
    private Dialog dialog = createTestDialog();
    private Message message = createTestMessage(dialog);
    private MessageDto messageDto = createMessageDto(dialog.getId());

    @BeforeEach
    public void beforeMethod(){
        dialogService = new DialogService(dialogRepository,dialogMapper,messageRepository, messageMapper, notificationHandler);
    }

    @Test
    void getDialogBetweenUsersInvokesDialogRepositoryFindAll() {
        List<Dialog> dialogs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            dialogs.add(dialog);
        }
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
            when(dialogRepository.findAll(any(Specification.class))).thenReturn(dialogs);
            when(dialogMapper.convertToDto(any(Dialog.class))).thenReturn(new DialogDto());
            dialogService.getDialogBetweenUsers(TEST_ID);
            verify(dialogRepository, times(1)).findAll(any(Specification.class));
        }
    }

    @Test
    void getDialogBetweenUsersInvokesDialogRepositorySave() {
        DialogDto dialogDto = createDialogDto();
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
            when(dialogRepository.findAll(any(Specification.class))).thenReturn(List.of());
            when(dialogMapper.convertToDto(any(Dialog.class))).thenReturn(dialogDto);
            when(dialogRepository.save(any(Dialog.class))).thenReturn(dialog);
            dialogService.getDialogBetweenUsers(TEST_ID);
            verify(dialogRepository, times(2)).save(any(Dialog.class));
        }
    }

    @Test
    void getMessagesToDialogInvokesMessageRepositoryFindAll() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            messages.add(message);
        }
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
            List<Message> list = new ArrayList<>();
            Page<Message> messagePage = new PageImpl<>(list);
            when(messageRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(messagePage);
            dialogService.getMessagesToDialog(TEST_ID, Pageable.ofSize(2));
            verify(messageRepository, times(1)).findAll(any(Specification.class),any(Pageable.class));
        }
    }

    @Test
    void getAllDialogsInvokesDialogRepositoryFindAll() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
            List<Dialog> dialogs = new ArrayList<>();
            Page<Dialog> dialogPage = new PageImpl<>(dialogs);
            when(dialogRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(dialogPage);
            dialogService.getAllDialogs(Pageable.ofSize(2));
            verify(dialogRepository, times(1)).findAll(any(Specification.class),any(Pageable.class));
        }
    }

    @Test
    void updateStatusMessageInvokesMessageRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            Message testMessage = createTestMessage(dialog);
            testMessage.setReadStatus(ReadStatus.SENT);
            dialog.setLastMessage(List.of(testMessage));
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
            when(dialogRepository.findById(anyLong())).thenReturn(Optional.of(dialog));
            dialogService.updateStatusMessage(TEST_ID);
            verify(messageRepository, times(1)).save(testMessage);
        }
    }

    @Test
    void getCountUnreadMessageInvokesMessageRepositoryCountByConversationPartner2AndReadStatusIs() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
            when(messageRepository.countByConversationPartner2AndReadStatusIs(anyLong(),any(ReadStatus.class))).thenReturn(TEST_SIZE);
            dialogService.getCountUnreadMessage();
            verify(messageRepository, times(1)).countByConversationPartner2AndReadStatusIs(anyLong(),any(ReadStatus.class));
        }
    }

//    @Test
//    void createMessageInvokesMessageRepositorySave() {
//        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
//            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_SECOND_ID);
//            when(messageMapper.convertToEntity(messageDto)).thenReturn(message);
//            when(dialogRepository.getById(anyLong())).thenReturn(any(Dialog.class));
//            when(messageMapper.convertToDto(any(Message.class))).thenReturn(any(MessageDto.class));
//            dialogService.createMessage(messageDto);
//            verify(messageRepository, times(1)).save(message);
//        }
//    }
}