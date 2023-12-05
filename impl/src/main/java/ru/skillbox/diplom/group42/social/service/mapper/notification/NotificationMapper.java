package ru.skillbox.diplom.group42.social.service.mapper.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationDto;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface NotificationMapper {

    NotificationDto convertToDTO(Notification notification);

    @Mapping(target = "recipientId", source = "receiverId")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "sentTime", expression = "java(ZonedDateTime.now())")
    Notification convertFromEventToEntity(EventNotificationDto eventNotificationDto);

}
