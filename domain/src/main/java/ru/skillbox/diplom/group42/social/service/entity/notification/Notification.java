package ru.skillbox.diplom.group42.social.service.entity.notification;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Column(name = "recipient_id")
    private Long recipientId;
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "content")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;
    @Column(name = "sent_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime sentTime;
}
