package ru.skillbox.diplom.group42.social.service.controller.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.notification.*;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@RequestMapping(ConstantURL.BASE_URL + "/notifications")
public interface NotificationController {

    @GetMapping("/settings")
    ResponseEntity<NotificationSettingDto> getNotificationsSettings();

    @PutMapping("/settings")
    ResponseEntity updateSettingsNotifications(@RequestBody NotificationSettingsUpdateDto notificationSettingsUpdateDto);

    @PutMapping("/readed")
    ResponseEntity setAllNotificationsReadStatus();

    @PostMapping("/settings{id}")
    ResponseEntity<Boolean> createSettingsNotifications(@PathVariable(value = "id") Long id);

    @PostMapping("/add")
    ResponseEntity addNotifications(@RequestBody EventNotificationDto eventNotificationDto);

    @GetMapping
    ResponseEntity<Page<NotificationsDto>> getNotifications(Pageable pageable);

    @GetMapping("/count")
    ResponseEntity<NotificationCountDto> getCountNotifications();


}
