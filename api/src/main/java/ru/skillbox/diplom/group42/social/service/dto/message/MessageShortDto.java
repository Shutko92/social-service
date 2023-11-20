package ru.skillbox.diplom.group42.social.service.dto.message;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MessageShortDto extends BaseDto {

    private ZonedDateTime time;
    private Long conversationPartner1;
    private Long conversationPartner2;
    private String messageText;
}
