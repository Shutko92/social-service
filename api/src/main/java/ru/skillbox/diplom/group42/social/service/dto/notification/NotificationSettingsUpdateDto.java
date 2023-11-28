package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingsUpdateDto {
    private Boolean enable;
    private NotificationType notificationType;

}
