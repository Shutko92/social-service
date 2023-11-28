package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventNotificationDto {
    private Long authorId;
    private Long receiverId;
    private NotificationType notificationType;
    private String content;

}
