package ru.skillbox.diplom.group42.social.service.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.exception.ResourceFoundException;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationSettingsRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationHandler {
    private final FriendRepository friendRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final KafkaTemplate<String, EventNotificationDto> eventNotificationDtoKafkaTemplate;
    private final JavaMailSender javaMailSender;
    private final AccountRepository accountRepository;

    @Value("${spring.mail.username}")
    String USER_NAME;

    public void sendNotifications(Long authorId, NotificationType notificationType, String content) {
        getRecipientId(authorId).forEach(recipientId -> {
            EventNotificationDto eventNotificationDto = createEventNotificationDto(authorId, recipientId, notificationType, getContent(content));
            if (checkSettingsNotification(recipientId, notificationType)) {
                eventNotificationDtoKafkaTemplate.send("notifications-handler", eventNotificationDto);
            }
            if (checkSettingsNotification(recipientId, NotificationType.SEND_EMAIL_MESSAGE)) {
                sendEmail(recipientId, getContent(content));
            }
        });


    }

    public void sendNotifications(Long authorId, Long recipientId, NotificationType notificationType, String content) {
        EventNotificationDto eventNotificationDto = createEventNotificationDto(authorId, recipientId, notificationType, getContent(content));
        if (checkSettingsNotification(recipientId, notificationType)) {
            eventNotificationDtoKafkaTemplate.send("notifications-handler", eventNotificationDto);
        }
        if (checkSettingsNotification(recipientId, NotificationType.SEND_EMAIL_MESSAGE)) {
            sendEmail(recipientId, getContent(content));
        }
    }

    private Set<Long> getRecipientId(Long authorId) {
        return friendRepository.findByIdFromOrIdToAndStatusCode(authorId, authorId, "FRIEND")
                .stream().map(Friend::getIdTo).filter(idFrom -> !idFrom.equals(authorId)).collect(Collectors.toSet());

    }

    private EventNotificationDto createEventNotificationDto(Long authorId, Long recipientId, NotificationType notificationType, String content) {
        return new EventNotificationDto(authorId, recipientId, notificationType, content);
    }

    private boolean checkSettingsNotification(Long recipientId, NotificationType notificationType) {
        NotificationSettings notificationSettings = notificationSettingsRepository.findByAccountId(recipientId).orElseThrow(() -> new ResourceFoundException(HttpStatus.NOT_FOUND, "Notification settings not found"));

        switch (notificationType) {
            case MESSAGE -> {
                return notificationSettings.getEnableMessage();
            }
            case POST -> {
                return notificationSettings.getEnablePost();
            }
            case POST_COMMENT -> {
                return notificationSettings.getEnablePostComment();
            }
            case COMMENT_COMMENT -> {
                return notificationSettings.getEnableCommentComment();
            }
            case FRIEND_BIRTHDAY -> {
                return notificationSettings.getEnableFriendBirthday();
            }
            case FRIEND_REQUEST -> {
                return notificationSettings.getEnableFriendRequest();
            }
            case SEND_EMAIL_MESSAGE -> {
                return notificationSettings.getEnableSendEmailMessage();
            }
            default -> {
                return false;
            }
        }

    }

    @Async
    public void sendEmail(Long recipientId, String content) {
        Account account = accountRepository.findById(recipientId).orElseThrow();
        String email = account.getEmail();
        if (email != null) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(USER_NAME);
            mailMessage.setTo(email);
            mailMessage.setText(content);
            mailMessage.setSubject("Code Lounge: Уведомление");
            javaMailSender.send(mailMessage);
        }
    }

    @Scheduled(cron = "@midnight")
    @Async
    public void sendNotificationAboutBirthDate() {
        List<Account> accounts = accountRepository.getAccountsByBirthDateMonthValueAndBirthDateDayOfMonth(ZonedDateTime.now().getMonthValue(), ZonedDateTime.now().getDayOfMonth());
        accounts.forEach(account -> sendNotifications(account.getId(), NotificationType.FRIEND_BIRTHDAY, "Сегодня день рождения!"));


    }

    private String getContent(String text) {
        if (text.length() > 30) {
            return text.substring(0, 30) + "...";
        } else {
            return text;
        }


    }

}

