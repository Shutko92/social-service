package ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag;

import lombok.Data;

@Data
public class StreamingMessageDto<T> {

    String type;

    Long recipientId;
    T data;


}
