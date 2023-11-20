package ru.skillbox.diplom.group42.social.service.entity.notification;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "notification_settings")
public class NotificationSettings extends BaseEntity {

    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "enable_post")
    private Boolean enablePost;
    @Column(name = "enable_post_comment")
    private Boolean enablePostComment;
    @Column(name = "enable_comment_comment")
    private Boolean enableCommentComment;
    @Column(name = "enable_message")
    private Boolean enableMessage;
    @Column(name = "enable_friend_request")
    private Boolean enableFriendRequest;
    @Column(name = "enable_friend_birthday")
    private Boolean enableFriendBirthday;
    @Column(name = "enable_send_email_message")
    private Boolean enableSendEmailMessage;

}
