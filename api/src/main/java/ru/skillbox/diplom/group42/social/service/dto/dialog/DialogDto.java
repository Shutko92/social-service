package ru.skillbox.diplom.group42.social.service.dto.dialog;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;

import java.util.List;

@Getter
@Setter
public class DialogDto extends BaseDto {

    private Integer unreadCount;
    private Long conversationPartner1;
    private Long conversationPartner2;
    private List<MessageDto> lastMessage;

    @Override
    public String toString() {
        return "DialogDto{" +
                "unreadCount=" + unreadCount +
                ", conversationPartner1=" + conversationPartner1 +
                ", conversationPartner2=" + conversationPartner2 +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
