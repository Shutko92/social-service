package ru.skillbox.diplom.group42.social.service.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamingMessageDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.*;
import ru.skillbox.diplom.group42.social.service.entity.notification.Notification;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.exception.ResourceFoundException;
import ru.skillbox.diplom.group42.social.service.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group42.social.service.mapper.notification.NotificationSettingsMapper;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationRepository;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "kafka-enable", havingValue = "true", matchIfMissing = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationSettingsMapper notificationSettingsMapper;
    private final KafkaTemplate<String, StreamingMessageDto<NotificationDto>> dtoKafkaTemplate;

    /**
     * Метод ищет записи о нотификациях пользователя через репозиторий, конвертирует результат в ответ.
     *
     * @param pageable параметр для разделения на страницы.
     * @return страничная информация о нотификациях.
     */
    public Page<NotificationsDto> getNotifications(Pageable pageable) {
        List<Notification> notificationList = notificationRepository.findAllByRecipientIdAndIsDeleted(SecurityUtil.getJwtUserIdFromSecurityContext(), false);
        return new PageImpl<>(notificationList.stream().map(notification -> {
            NotificationsDto notificationsDto = new NotificationsDto();
            notificationsDto.setTimeStamp(ZonedDateTime.now());
            notificationsDto.setData(notificationMapper.convertToDTO(notification));
            notification.setIsDeleted(true);
            notificationRepository.save(notification);
            return notificationsDto;

        }).collect(Collectors.toList()), pageable, notificationList.size());
    }

    /**
     * Метод ищет настройки нотификаций пользователя через репозиторий или выбрасывает исключение.
     *
     * @return информация о настройках нотификаций.
     */
    public NotificationSettingDto getNotificationsSettings() {
        return notificationSettingsMapper.convertToDTO(notificationSettingsRepository.findByAccountId(SecurityUtil.getJwtUserIdFromSecurityContext())
                .orElseThrow(() -> new ResourceFoundException(HttpStatus.NOT_FOUND
                        , "Notification Settings from account Id "
                        + SecurityUtil.getJwtUserIdFromSecurityContext() + " not found")));

    }

    /**
     * Метод ищет настройки нотификаций пользователя через репозиторий или выбрасывает исключение, сравнивает с вариантами нотификаций,
     * при совпадении активирует, сохраняет результат.
     *
     * @param notificationSettingsUpdateDto параметры обновления нотификаций.
     */
    public void updateSettingsNotifications(NotificationSettingsUpdateDto notificationSettingsUpdateDto) {
        NotificationSettings notificationSettings = notificationSettingsRepository
                .findByAccountId(SecurityUtil.getJwtUserIdFromSecurityContext())
                .orElseThrow(() -> new ResourceFoundException(HttpStatus.NOT_FOUND
                        , "Notification Settings from account Id "
                        + SecurityUtil.getJwtUserIdFromSecurityContext() + " not found"));

        switch (notificationSettingsUpdateDto.getNotificationType()) {
            case POST_COMMENT -> notificationSettings.setEnablePostComment(notificationSettingsUpdateDto.getEnable());
            case POST -> notificationSettings.setEnablePost(notificationSettingsUpdateDto.getEnable());
            case COMMENT_COMMENT ->
                    notificationSettings.setEnableCommentComment(notificationSettingsUpdateDto.getEnable());
            case FRIEND_REQUEST ->
                    notificationSettings.setEnableFriendRequest(notificationSettingsUpdateDto.getEnable());
            case MESSAGE -> notificationSettings.setEnableMessage(notificationSettingsUpdateDto.getEnable());
            case FRIEND_BIRTHDAY ->
                    notificationSettings.setEnableFriendBirthday(notificationSettingsUpdateDto.getEnable());
            case SEND_EMAIL_MESSAGE ->
                    notificationSettings.setEnableSendEmailMessage(notificationSettingsUpdateDto.getEnable());
        }
        notificationSettingsRepository.save(notificationSettings);
    }

    /**
     * Метод считает нотификации пользователя через репозиторий, формирует ответ с фиксированным временем.
     *
     * @return счет нотификаций.
     */
    public NotificationCountDto getCountNotifications() {
        Integer countNotifications = notificationRepository.countByRecipientIdAndIsDeleted(SecurityUtil.getJwtUserIdFromSecurityContext(), false);
        Count count = new Count();
        count.setCount(countNotifications);
        NotificationCountDto notificationCountDto = new NotificationCountDto();
        notificationCountDto.setTimeStamp(ZonedDateTime.now());
        notificationCountDto.setData(count);
        return notificationCountDto;
    }

    /**
     * Метод формирует сообщение, маркирует его, как нотификацию, конвертирует параметры в объект, сохраняет,
     * вставляет инвормацию в сообщение, отправляет его.
     *
     * @param eventNotificationDto параметры нотификаций событий.
     */
    @KafkaListener(topics = "notifications-handler", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void createNotifications(EventNotificationDto eventNotificationDto) {
        StreamingMessageDto<NotificationDto> streamingMessageDto = new StreamingMessageDto<>();
        streamingMessageDto.setType("NOTIFICATION");
        streamingMessageDto.setRecipientId(eventNotificationDto.getReceiverId());
        streamingMessageDto.setData(notificationMapper.convertToDTO(notificationRepository.save(notificationMapper.convertFromEventToEntity(eventNotificationDto))));
        dtoKafkaTemplate.send("sending-notifications", streamingMessageDto);
    }

    /**
     * Метод ищет нотификации пользователя через репозиторий, проставляет каждой статус удаления, сохраняет результат.
     */
    public void updateStatusRead() {
        List<Notification> notificationList = notificationRepository.findAllByRecipientIdAndIsDeleted(SecurityUtil.getJwtUserIdFromSecurityContext(), false);
        notificationList.forEach(notification -> notification.setIsDeleted(true));
        notificationRepository.saveAll(notificationList);
    }

}
