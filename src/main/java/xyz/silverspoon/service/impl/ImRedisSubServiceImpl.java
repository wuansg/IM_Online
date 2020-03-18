package xyz.silverspoon.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.bean.ImNotification;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelationRequest;
import xyz.silverspoon.component.ImSocketManager;
import xyz.silverspoon.component.WSResult;
import xyz.silverspoon.dto.ImRequestDto;
import xyz.silverspoon.service.ImRedisSubService;
import xyz.silverspoon.service.ImUserRelationRequestService;
import xyz.silverspoon.service.ImUserService;
import xyz.silverspoon.utils.UUIDType;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

@Slf4j
@Component
public class ImRedisSubServiceImpl extends MessageListenerAdapter{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ImUserService userService;
    @Autowired
    private ImUserRelationRequestService  requestService;

    public void receiveMessage(String message){
        log.info("订阅消息:{}", message);
        ImMessage imMessage = null;
        try {
            imMessage = new ObjectMapper().readValue(message, ImMessage.class);
            WebSocketSession session = ImSocketManager.get(imMessage.getReceiverID());
            log.info("获取到WebSocketSession{}",session);
            if (session != null) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("开始解析消息{}, {}", message, pattern);
        Object object = redisTemplate.getValueSerializer().deserialize(message.getBody());
        if (object == null) {
            return;
        }
        if (object instanceof ImMessage) {
            ImMessage imMessage = (ImMessage) object;
            WSResult<ImMessage> result = new WSResult<>();
            result.setType(UUIDType.IM_MESSAGE.getType());
            result.setData(imMessage);
            log.info("解析消息成功{}", imMessage);
            WebSocketSession session = ImSocketManager.get(imMessage.getReceiverID());
            if (session == null){
                log.info("找不到对应的websocketSession");
                return;
            }
            try {
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(result)));
                return;
            } catch (IOException e) {
                log.error("发送消息出错{}", e.getMessage());
            }
        }else if (object instanceof ImNotification) {
            ImNotification notification = (ImNotification)object;
            log.info("解析消息成功{}", notification);
            ImUserRelationRequest request = requestService.getRequestByUUID(notification.getContent());
            ImUser user = userService.getUserByUUID(request.getStatus() == 0 ? request.getRequestID() : request.getAcceptID());
            ImRequestDto requestDto = new ImRequestDto();
            requestDto.setUUID(notification.getContent());
            requestDto.setStatus(request.getStatus());
            requestDto.setRequestUser(user);
            try {
                notification.setContent(new ObjectMapper().writeValueAsString(requestDto));
            } catch (JsonProcessingException e) {
                log.error("{}", e.getMessage());
            }
            WSResult<ImNotification> result = new WSResult<>();
            result.setType(UUIDType.IM_NOTIFICATION.getType());
            result.setData(notification);
            WebSocketSession session = ImSocketManager.get(notification.getUserID());
            if (session == null) {
                log.info("找不到对应的websocketSession");
                return;
            }
            try {
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(result)));
                return;
            }catch (IOException e) {
                log.error("发送消息出错{}", e.getMessage());
            }
        }
        log.error("解析消息出错，消息内容格式不符合{}", message.getBody());
    }
}
