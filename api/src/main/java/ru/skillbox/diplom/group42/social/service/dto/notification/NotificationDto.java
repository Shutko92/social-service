package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class NotificationDto {
    private Long id;
    private Long authorId;
    private String content;
    private NotificationType notificationType;
    private ZonedDateTime sentTime;

}
