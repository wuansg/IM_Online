package xyz.silverspoon.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.service.ImMessageService;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class WSHandler extends TextWebSocketHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ImMessageService messageService;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(session.getUri().getPath());
        matcher.find();
        String uuid = matcher.group();
        log.info("websocket {} 连接建立成功{}", uuid, session);
        ImSocketManager.add(uuid, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        ImMessage imMessage = new ObjectMapper().readValue(message.getPayload(), ImMessage.class);
        imMessage.setUUID(uuidGenerator.generateUUID(UUIDType.IM_MESSAGE));
        log.info("接收到信息 {}", imMessage);
        messageService.saveMessage(imMessage);
        redisTemplate.convertAndSend("im_message", imMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        log.error("出现错误{}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        session.close();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(session.getUri().getPath());
        matcher.find();
        String uuid = matcher.group();
        log.info("websocket连接关闭{}", uuid);
        ImSocketManager.remove(uuid);
    }
}
