package ru.skillbox.diplom.group42.social.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamingMessageDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationSettingDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationSettingsUpdateDto;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group42.social.service.mapper.notification.NotificationSettingsMapper;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationRepository;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationService;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationSettingsRepository notificationSettingsRepository;
    @Mock
    private NotificationMapper notificationMapper;
    @Mock
    private NotificationSettingsMapper notificationSettingsMapper;
    @Mock
    private KafkaTemplate<String, StreamingMessageDto<NotificationDto>> kafkaTemplate;
    private Notification testNotification = createTestNotification();
    private NotificationDto notificationDto = createNotificationDto();

    private NotificationService service;
    @BeforeEach
    public void beforeMethod() {
        service = new NotificationService(notificationRepository, notificationSettingsRepository, notificationMapper, notificationSettingsMapper, kafkaTemplate);
    }

    @Test
    @Order(2)
    void getNotifications() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            when(notificationRepository.findAllByRecipientIdAndIsDeleted(anyLong(), anyBoolean())).thenReturn(List.of(testNotification));
            when(notificationMapper.convertToDTO(testNotification)).thenReturn(notificationDto);
            service.getNotifications(Pageable.ofSize(2));
            verify(notificationRepository, times(1)).save(testNotification);
        }
    }

    @Test
    @Order(4)
    void getNotificationsSettings() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            NotificationSettings testNotificationSettings = createTestNotificationSettings();
            NotificationSettingDto notificationSettingDto = createNotificationSettingDto();
            when(notificationSettingsRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testNotificationSettings));
            when(notificationSettingsMapper.convertToDTO(testNotificationSettings)).thenReturn(notificationSettingDto);
            service.getNotificationsSettings();
            verify(notificationSettingsMapper, times(1)).convertToDTO(testNotificationSettings);
        }

    }

    @Test
    @Order(5)
    void updateSettingsNotifications() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            NotificationSettings testNotificationSettings = createTestNotificationSettings();
            NotificationSettingsUpdateDto notificationSettingsUpdateDto = createNotificationSettingsUpdateDto();
            when(notificationSettingsRepository.findByAccountId(TEST_ACCOUNT_ID)).thenReturn(Optional.of(testNotificationSettings));
            service.updateSettingsNotifications(notificationSettingsUpdateDto);
            verify(notificationSettingsRepository, times(1)).save(testNotificationSettings);
        }
    }

    @Test
    @Order(3)
    void getCountNotifications() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            when(notificationRepository.countByRecipientIdAndIsDeleted(anyLong(), anyBoolean())).thenReturn(1);
            service.getCountNotifications();
            verify(notificationRepository, times(1)).countByRecipientIdAndIsDeleted(anyLong(), anyBoolean());
        }

    }

    @Test
    @Order(1)
    void createNotifications() {
        EventNotificationDto eventNotificationDto = createEventNotificationDto();
        when(notificationMapper.convertFromEventToEntity(eventNotificationDto)).thenReturn(testNotification);
        when(notificationRepository.save(testNotification)).thenReturn(testNotification);
        when(notificationMapper.convertToDTO(testNotification)).thenReturn(notificationDto);
        service.createNotifications(eventNotificationDto);
        verify(kafkaTemplate, times(1)).send(anyString(), any(StreamingMessageDto.class));
    }

    @Test
    void updateStatusRead() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            when(notificationRepository.findAllByRecipientIdAndIsDeleted(anyLong(), anyBoolean())).thenReturn(List.of(testNotification));
            service.updateStatusRead();
            verify(notificationRepository, times(1)).saveAll(List.of(testNotification));
        }
    }
}