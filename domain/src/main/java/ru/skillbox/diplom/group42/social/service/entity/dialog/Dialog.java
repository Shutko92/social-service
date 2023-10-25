package ru.skillbox.diplom.group42.social.service.entity.dialog;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "dialog")
public class Dialog extends BaseEntity {

    @Column(name = "unread_count")
    private Integer unreadCount;

    @Column(name = "conversation_partner1")
    private Long conversationPartner1;

    @Column(name = "conversation_partner2")
    private Long conversationPartner2;

    @OneToMany(mappedBy = "dialogId")
    private List<Message> lastMessage;

}
