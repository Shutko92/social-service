package ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag;

import lombok.Data;

@Data
public class StreamMessageDataDto {

    private String time;
    private Long conversationPartner1;
    private Long conversationPartner2;
    private String messageText;
    private String readStatus;
    private Long dialogId;

}
