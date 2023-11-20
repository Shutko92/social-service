package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class NotificationCountDto {

    private ZonedDateTime timeStamp;

    private Count data;
}
