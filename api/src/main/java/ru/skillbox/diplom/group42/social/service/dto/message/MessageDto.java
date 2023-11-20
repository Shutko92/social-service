package ru.skillbox.diplom.group42.social.service.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.group42.social.service.dto.base.BaseDto;

import java.time.ZonedDateTime;

@Data
public class MessageDto extends BaseDto {

    @JsonProperty("time")
    private ZonedDateTime time;
    @JsonProperty("conversationPartner1")
    private Long conversationPartner1;
    @JsonProperty("conversationPartner2")
    private Long conversationPartner2;
    @JsonProperty("messageText")
    private String messageText;
    @JsonProperty("readStatus")
    private ReadStatus readStatus;
    @JsonProperty("dialogId")
    private Long dialogId;


}
