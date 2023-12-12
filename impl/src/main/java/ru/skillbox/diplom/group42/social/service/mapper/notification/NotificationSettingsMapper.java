package ru.skillbox.diplom.group42.social.service.mapper.notification;

import org.mapstruct.Mapper;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationSettingDto;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;

@Mapper(componentModel = "spring")
public interface NotificationSettingsMapper {

    NotificationSettingDto convertToDTO(NotificationSettings notificationSettings);

}
