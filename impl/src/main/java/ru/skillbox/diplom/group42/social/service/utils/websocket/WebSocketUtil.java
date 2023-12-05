package ru.skillbox.diplom.group42.social.service.utils.websocket;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@Slf4j
public class WebSocketUtil {
    private static final ConcurrentHashMap<Long, WebSocketSession> webSocketSession = new ConcurrentHashMap<>();

    public static void addSession(Long id, WebSocketSession session) {
        webSocketSession.put(id, session);
    }

    public static void deleteSession(Long id) {
        webSocketSession.remove(id);
    }

    public static WebSocketSession getSession(Long id) {
        return webSocketSession.get(id);
    }

    public boolean containsSession(Long id) {
        return webSocketSession.containsKey(id);
    }


}
