package ru.skillbox.diplom.group42.social.service.config.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.skillbox.diplom.group42.social.service.dto.message.MessageDto;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamMessageDataDto;
import ru.skillbox.diplom.group42.social.service.dto.message.streamingMessag.StreamingMessageDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.EventNotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationDto;
import ru.skillbox.diplom.group42.social.service.dto.notification.NotificationType;
import ru.skillbox.diplom.group42.social.service.mapper.message.MessageMapper;
import ru.skillbox.diplom.group42.social.service.security.JwtTokenProvider;
import ru.skillbox.diplom.group42.social.service.service.account.AccountService;
import ru.skillbox.diplom.group42.social.service.service.dialog.DialogService;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;
import ru.skillbox.diplom.group42.social.service.utils.websocket.WebSocketUtil;

import java.time.ZonedDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "kafka-enable", havingValue = "true", matchIfMissing = true)
public class WebSocketHandler extends TextWebSocketHandler {

    private final JwtTokenProvider tokenProvider;
    private final MessageMapper messageMapper;
    private final DialogService dialogService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, MessageDto> messageDtoKafkaTemplate;
    private final AccountService accountService;


    public void sendMessage(StreamingMessageDto<StreamMessageDataDto> dto) throws Exception {
        if (WebSocketUtil.containsSession(dto.getData().getConversationPartner2())) {
            WebSocketUtil.getSession(dto.getRecipientId()).sendMessage(new TextMessage(objectMapper.writeValueAsString(dto)));
            log.info("sendMessage dto ->  " + dto);
        }

    }

    @KafkaListener(topics = "sending-notifications", containerFactory = "sendKafkaListenerContainerFactory")
    public void sendNotifications(StreamingMessageDto<NotificationDto> streamingMessageDto) throws Exception {
        log.info("sendNotifications   streamingMessageDto " + streamingMessageDto.toString());

        if (WebSocketUtil.containsSession(streamingMessageDto.getRecipientId())) {
            log.info("sendNotifications   streamingMessageDto " + streamingMessageDto.toString());
            WebSocketUtil.getSession(streamingMessageDto.getRecipientId()).sendMessage(new TextMessage(objectMapper.writeValueAsString(streamingMessageDto)));
        }


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("handleTextMessage" + payload);
        JsonNode jsonNode = objectMapper.readTree(payload);
        if (jsonNode.get("type").textValue().equals("MESSAGE")) {
            MessageDto messageDto = objectMapper.readValue(jsonNode.get("data").toString(), MessageDto.class);
            messageDto.setConversationPartner1(tokenProvider.getUserIdFromToken(session));
            messageDto.setConversationPartner2(objectMapper.readValue(jsonNode.get("recipientId").toString(), Long.class));
            messageDtoKafkaTemplate.send("send-message", messageDto);
            sendMessage(getStreamingMessageDto(dialogService.createMessage(messageDto)));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketUtil.addSession(tokenProvider.getUserIdFromToken(session), session);
        log.info("afterConnectionEstablished add webSocketSession id"
                + tokenProvider.getUserIdFromToken(session) + " webSocketSession " + session);
        session.sendMessage(new TextMessage("Hello, i am websocket"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("afterConnectionClosed " + session + " id" + tokenProvider.getUserIdFromToken(session));
        Long userId  = tokenProvider.getUserIdFromToken(session);
        accountService.setAccountOnlineStatus(userId,false);

        WebSocketUtil.deleteSession(tokenProvider.getUserIdFromToken(session));
    }

    private StreamingMessageDto<StreamMessageDataDto> getStreamingMessageDto(MessageDto messageDto) {
        StreamMessageDataDto streamMessageDataDto = messageMapper.convertToStreamDto(messageDto);
        StreamingMessageDto<StreamMessageDataDto> streamingMessageDto = new StreamingMessageDto<>();
        streamingMessageDto.setType("MESSAGE");
        streamingMessageDto.setRecipientId(messageDto.getConversationPartner2());
        streamingMessageDto.setData(streamMessageDataDto);
        log.info("streamingMessageDto -- " + streamingMessageDto);
        return streamingMessageDto;
    }

    @KafkaListener(topics = "send-message", containerFactory = "kafkaListenerMessageContainerFactory")
    private void ConsumerListenerKafka(MessageDto messageDto) {
        log.info("ConsumerListenerKafka " + messageDto.toString());
    }


}
