package ru.skillbox.diplom.group42.social.service.mapper.message;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageShortDto;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamMessageDataDto;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface MessageMapper {

    @Mapping(target = "dialogId", expression = "java(message.getDialogId().getId())")
    MessageDto convertToDto(Message message);

    @Mapping(target = "dialogId", ignore = true)
    List<MessageDto> convertToListDTO(List<Message> messageList);

    MessageShortDto convertToShortDto(Message message);

    @Mapping(target = "time", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "readStatus", expression = "java(ru.skillbox.diplom.group42.social.service.dto.message.ReadStatus.SENT)")
    @Mapping(target = "dialogId", ignore = true)
    Message convertToEntity(MessageDto messageDto);


    StreamMessageDataDto convertToStreamDto(MessageDto messageDto);
}
