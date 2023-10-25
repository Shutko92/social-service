package ru.skillbox.diplom.group42.social.service.dto.dialog;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;
@Getter
@Setter
public class DialogSearchDto extends BaseSearchDto {
    private Integer unreadCount;
    private Long conversationPartner1;
    private Long conversationPartner2;

    @Override
    public String toString() {
        return "DialogSearchDto{" +
                "unreadCount=" + unreadCount +
                ", conversationPartner1=" + conversationPartner1 +
                ", conversationPartner2=" + conversationPartner2 +
                '}';
    }
}
