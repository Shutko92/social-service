package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class NotificationsDto {

    private ZonedDateTime timeStamp;

    private NotificationDto data;


}
