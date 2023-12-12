package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationSettingDto;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.mapper.notification.NotificationSettingsMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createNotificationSettingDto;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTestNotificationSettings;

class NotificationSettingsMapperTest {
    private final NotificationSettingsMapper mapper = Mappers.getMapper(NotificationSettingsMapper.class);

    @Test
    void convertToDTO() {
        NotificationSettings testNotificationSetting = createTestNotificationSettings();
        NotificationSettingDto notificationSettingDto = mapper.convertToDTO(testNotificationSetting);
        assertEquals(testNotificationSetting.getId(), notificationSettingDto.getId());
        assertEquals(testNotificationSetting.getEnableMessage(), notificationSettingDto.getEnableMessage());
        assertEquals(testNotificationSetting.getEnablePost(), notificationSettingDto.getEnablePost());
        assertEquals(testNotificationSetting.getEnableFriendBirthday(), notificationSettingDto.getEnableFriendBirthday());
        assertEquals(testNotificationSetting.getEnableCommentComment(), notificationSettingDto.getEnableCommentComment());
        assertEquals(testNotificationSetting.getEnableFriendRequest(), notificationSettingDto.getEnableFriendRequest());
        assertEquals(testNotificationSetting.getEnablePostComment(), notificationSettingDto.getEnablePostComment());
        assertEquals(testNotificationSetting.getEnableSendEmailMessage(), notificationSettingDto.getEnableSendEmailMessage());
    }

}