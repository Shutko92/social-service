package ru.skillbox.diplom.group42.social.service.mapper.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationSettingDto;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;

@Mapper(componentModel = "spring")
public interface NotificationSettingsMapper {

    NotificationSettingDto convertToDTO(NotificationSettings notificationSettings);
    @Mapping(target = "isDeleted", constant = "false")
    NotificationSettings convertToEntity(NotificationSettingDto notificationSettingDto);

}
