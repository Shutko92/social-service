package ru.skillbox.diplom.group42.social.service.dto.message;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseSearchDto;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MessageSearchDto extends BaseSearchDto {

    private ZonedDateTime time;
    private Long conversationPartner1;
    private Long conversationPartner2;
    private String messageText;
    private ReadStatus readStatus;
    private Long dialogId;
}
