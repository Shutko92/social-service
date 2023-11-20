package ru.skillbox.diplom.group42.social.service.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationHandler {
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, EventNotificationDto> eventNotificationDtoKafkaTemplate;


    public void sendNotifications(Long authorId, NotificationType notificationType, String content) {
        getRecipientId().forEach(recipientId -> {
            EventNotificationDto eventNotificationDto = new EventNotificationDto();
            eventNotificationDto.setNotificationType(notificationType);
            eventNotificationDto.setContent(content);
            eventNotificationDto.setAuthorId(authorId);
            eventNotificationDto.setReceiverId(recipientId);
            eventNotificationDtoKafkaTemplate.send("notifications-handler", eventNotificationDto);
        });


    }

    public void sendNotifications(Long authorId, Long recipientId, NotificationType notificationType, String content) {
        EventNotificationDto eventNotificationDto = new EventNotificationDto();
        eventNotificationDto.setNotificationType(notificationType);
        eventNotificationDto.setContent(content);
        eventNotificationDto.setAuthorId(authorId);
        eventNotificationDto.setReceiverId(recipientId);
        eventNotificationDtoKafkaTemplate.send("notifications-handler", eventNotificationDto);

    }


    private List<Long> getRecipientId() {
        return accountRepository.findAll().stream().map(BaseEntity::getId).collect(Collectors.toList());

    }

}

