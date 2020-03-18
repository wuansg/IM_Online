package xyz.silverspoon.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ImSocketManager {
    private static ConcurrentHashMap<String, WebSocketSession> sessions =
            new ConcurrentHashMap<>();
    public static void add(String key, WebSocketSession session) {
        log.info("新添加webSocket连接 {}", key);
        sessions.put(key, session);
    }

    public static void remove(String key) {
        log.info("移除webSocket连接 {}", key);
        sessions.remove(key);
    }

    public static WebSocketSession get(String key) {
        log.info("获取webSocket连接 {}", key);
        return sessions.get(key);
    }
}
