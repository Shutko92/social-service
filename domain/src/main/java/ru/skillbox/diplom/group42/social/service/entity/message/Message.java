package ru.skillbox.diplom.group42.social.service.entity.message;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.message.ReadStatus;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;

    @Column(name = "conversation_partner1")
    private Long conversationPartner1;

    @Column(name = "conversation_partner2")
    private Long conversationPartner2;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "read_status")
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @ManyToOne()
    @JoinColumn(name = "dialog_id", referencedColumnName = "id")
    private Dialog dialogId;

}
