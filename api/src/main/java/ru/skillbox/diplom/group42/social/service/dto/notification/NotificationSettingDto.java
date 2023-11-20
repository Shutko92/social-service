package ru.skillbox.diplom.group42.social.service.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingDto {

    private Long id;
    private Boolean enablePost;
    private Boolean enablePostComment;
    private Boolean enableCommentComment;
    private Boolean enableMessage;
    private Boolean enableFriendRequest;
    private Boolean enableFriendBirthday;
    private Boolean enableSendEmailMessage;


}
