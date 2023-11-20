package ru.skillbox.diplom.group42.social.service.controller.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.notification.*;
import ru.skillbox.diplom.group42.social.service.service.notification.NotificationService;

@RestController
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<NotificationSettingDto> getNotificationsSettings() {
        return ResponseEntity.ok(notificationService.getNotificationsSettings());
    }

    @Override
    public ResponseEntity updateSettingsNotifications(NotificationSettingsUpdateDto notificationSettingsUpdateDto) {
        notificationService.updateSettingsNotifications(notificationSettingsUpdateDto);
        return ResponseEntity.ok().build();
    }

    //TODO не нужен?
    @Override
    public ResponseEntity setAllNotificationsReadStatus() {
        return null;
    }

    //TODO не нужен?
    @Override
    public ResponseEntity<Boolean> createSettingsNotifications(Long id) {
        return null;
    }

    //TODO не нужен
    @Override
    public ResponseEntity addNotifications(EventNotificationDto eventNotificationDto) {
      notificationService.addNotifications(eventNotificationDto);
      return ResponseEntity.ok().build();
    }

    @Override
    public  ResponseEntity<Page<NotificationsDto>> getNotifications(Pageable pageable) {

        return ResponseEntity.ok(notificationService.getNotifications(pageable));
    }

    @Override
    public ResponseEntity<NotificationCountDto> getCountNotifications() {
        return ResponseEntity.ok(notificationService.getCountNotifications());
    }
}
