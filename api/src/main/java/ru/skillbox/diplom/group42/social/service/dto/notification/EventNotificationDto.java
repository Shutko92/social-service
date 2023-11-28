package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class EventNotificationDto {
    private Long authorId;
    private Long receiverId;
    private NotificationType notificationType;
    private String content;

}
