package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageShortDto;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamMessageDataDto;
import ru.skillbox.diplom.group42.social.service.entity.dialog.Dialog;
import ru.skillbox.diplom.group42.social.service.entity.message.Message;
import ru.skillbox.diplom.group42.social.service.mapper.message.MessageMapper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createMessageDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createTestMessage;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;

class MessageMapperTest {
    private MessageMapper mapper = Mappers.getMapper(MessageMapper.class);

    @Test
    void convertToDto() {
        Dialog dialog = new Dialog();
        dialog.setId(TEST_ID);
        Message message = createTestMessage(dialog);
        MessageDto messageDto = mapper.convertToDto(message);
        assertEquals(message.getId(), messageDto.getId());
        assertEquals(message.getReadStatus(), messageDto.getReadStatus());
        assertEquals(message.getMessageText(), messageDto.getMessageText());
        assertEquals(message.getConversationPartner1(), messageDto.getConversationPartner1());
        assertEquals(message.getConversationPartner2(), messageDto.getConversationPartner2());
        assertEquals(message.getTime(), messageDto.getTime());
        assertEquals(dialog.getId(), messageDto.getDialogId());
        assertEquals(message.getIsDeleted(), messageDto.getIsDeleted());
    }

    @Test
    void convertToListDTO() {
        List<Message> messageList = new ArrayList<>();
        Dialog dialog = new Dialog();
        Message message = createTestMessage(dialog);
        dialog.setId(TEST_ID);
        for (int i = 0; i < 3; i++) {
            messageList.add(message);
        }
        List<MessageDto> messageDtoList = mapper.convertToListDTO(messageList);
        for (MessageDto messageDto : messageDtoList) {
            assertEquals(message.getId(), messageDto.getId());
            assertEquals(message.getReadStatus(), messageDto.getReadStatus());
            assertEquals(message.getMessageText(), messageDto.getMessageText());
            assertEquals(message.getConversationPartner1(), messageDto.getConversationPartner1());
            assertEquals(message.getConversationPartner2(), messageDto.getConversationPartner2());
            assertEquals(message.getTime(), messageDto.getTime());
            assertEquals(dialog.getId(), messageDto.getDialogId());
            assertEquals(message.getIsDeleted(), messageDto.getIsDeleted());
        }
    }

    @Test
    void convertToShortDto() {
        Dialog dialog = new Dialog();
        dialog.setId(TEST_ID);
        Message message = createTestMessage(dialog);
        MessageShortDto messageShortDto = mapper.convertToShortDto(message);
        assertEquals(message.getId(), messageShortDto.getId());
        assertEquals(message.getMessageText(), messageShortDto.getMessageText());
        assertEquals(message.getTime(), messageShortDto.getTime());
        assertEquals(message.getConversationPartner1(), messageShortDto.getConversationPartner1());
        assertEquals(message.getConversationPartner2(), messageShortDto.getConversationPartner2());
        assertEquals(message.getIsDeleted(), messageShortDto.getIsDeleted());
    }

    @Test
    void convertToEntity() {
        MessageDto messageDto = createMessageDto(TEST_ID);
        messageDto.setTime(ZonedDateTime.now());
        Message message = mapper.convertToEntity(messageDto);
        assertEquals(message.getId(), messageDto.getId());
        assertEquals(message.getReadStatus(), messageDto.getReadStatus());
        assertEquals(message.getMessageText(), messageDto.getMessageText());
        assertEquals(message.getConversationPartner1(), messageDto.getConversationPartner1());
        assertEquals(message.getConversationPartner2(), messageDto.getConversationPartner2());
//        assertEquals(message.getTime(), messageDto.getTime());
        assertEquals(message.getId(), messageDto.getDialogId());
        assertEquals(message.getIsDeleted(), messageDto.getIsDeleted());
    }

    @Test
    void convertToStreamDto() {
        MessageDto messageDto = createMessageDto(TEST_ID);
        StreamMessageDataDto streamMessageDataDto = mapper.convertToStreamDto(messageDto);
        assertEquals(ZonedDateTime.parse(streamMessageDataDto.getTime()), messageDto.getTime());
        assertEquals(streamMessageDataDto.getDialogId(), messageDto.getDialogId());
        assertEquals(streamMessageDataDto.getMessageText(), messageDto.getMessageText());
        assertEquals(streamMessageDataDto.getReadStatus(), messageDto.getReadStatus().toString());
        assertEquals(streamMessageDataDto.getConversationPartner1(), messageDto.getConversationPartner1());
        assertEquals(streamMessageDataDto.getConversationPartner2(), messageDto.getConversationPartner2());
    }
}