package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationDto;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;
import ru.skillbox.diplom.group42.social.service.mapper.notification.NotificationMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;

class NotificationMapperTest {
    private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Test
    void convertToDTO() {
        Notification testNotification = createTestNotification();
        NotificationDto notificationDto = mapper.convertToDTO(testNotification);
        assertEquals(testNotification.getId(), notificationDto.getId());
        assertEquals(testNotification.getNotificationType(), notificationDto.getNotificationType());
        assertEquals(testNotification.getAuthorId(), notificationDto.getAuthorId());
        assertEquals(testNotification.getContent(), notificationDto.getContent());
        assertEquals(testNotification.getSentTime(), notificationDto.getSentTime());
    }

    @Test
    void convertToEntity() {
        NotificationDto notificationDto = createNotificationDto();
        Notification notification = mapper.convertToEntity(notificationDto);
        assertEquals(notificationDto.getId(), notification.getId());
        assertEquals(notificationDto.getNotificationType(), notification.getNotificationType());
        assertEquals(notificationDto.getContent(), notification.getContent());
        assertEquals(notificationDto.getSentTime(), notification.getSentTime());
        assertEquals(notificationDto.getAuthorId(), notification.getAuthorId());
    }

    @Test
    void convertFromEventToEntity() {
        EventNotificationDto eventNotificationDto = createEventNotificationDto();
        Notification notification = mapper.convertFromEventToEntity(eventNotificationDto);
        assertEquals(eventNotificationDto.getAuthorId(), notification.getAuthorId());
        assertEquals(eventNotificationDto.getReceiverId(), notification.getRecipientId());
        assertEquals(eventNotificationDto.getNotificationType(), notification.getNotificationType());
        assertEquals(eventNotificationDto.getContent(), notification.getContent());
    }
}