package xyz.silverspoon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.dto.ImMessageDto;
import xyz.silverspoon.dto.ImUserDto;
import xyz.silverspoon.param.UserMessageParam;
import xyz.silverspoon.repository.ImMessageRepository;
import xyz.silverspoon.repository.ImUserRepository;
import xyz.silverspoon.service.ImMessageService;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ImMessageServiceImpl implements ImMessageService {

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Value(value = "${im.redis.unreads.key.prefix:}")
    private String UNREADS_PREFIX;

    @Autowired
    private ImMessageRepository messageRepository;

    @Autowired
    private ImUserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public ImMessageServiceImpl() {
    }

    @Override
    public List<ImMessage> listMessage(String uuid) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("SenderID").is(uuid), Criteria.where("ReceiverID").is(uuid));
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "time"))
                .with(PageRequest.of(0, 10));
        return messageRepository.listMessages(query);
    }

    @Override
    public List<ImMessage> listUserMessage(UserMessageParam messageParam) {
        Query query = new Query(Criteria.where("SenderID").in(messageParam.getSenderID(), messageParam.getReceiverID())
                                .and("ReceiverID").in(messageParam.getSenderID(), messageParam.getReceiverID()))
                .with(Sort.by(Sort.Direction.DESC, "time"))
                .with(PageRequest.of(messageParam.getPage(), messageParam.getSize()));
        return messageRepository.listMessages(query);
    }

    @Override
    public ImMessage saveMessage(ImMessage message) {
        message.setUUID(uuidGenerator.generateUUID(UUIDType.IM_MESSAGE));
        message = messageRepository.save(message);
//        redisTemplate.expire(UNREADS_PREFIX+message.getReceiverID(), 30, TimeUnit.MINUTES);
        redisTemplate.opsForList().rightPush(UNREADS_PREFIX+message.getReceiverID(), message);
        return message;
    }

    @Override
    public List<ImMessageDto> getUnreads(String uuid) {
        List<Object> messages = redisTemplate.opsForList().range(UNREADS_PREFIX+uuid, 0, -1);
        if (!CollectionUtils.isEmpty(messages)) {
            redisTemplate.opsForList().trim(UNREADS_PREFIX + uuid, messages.size(), messages.size());
            Map<String, ImMessageDto> map = new HashMap<>();
            messages.forEach(o -> {
                if (o instanceof ImMessage) {
                    ImMessage message = (ImMessage) o;
                    String userID = message.getSenderID().equals(uuid) ? message.getReceiverID() : message.getSenderID();
                    ImMessageDto messageDto = map.get(userID);
                    if (messageDto == null) {
                        Query query = new Query(Criteria.where("UUID").is(userID));
                        ImUser user = userRepository.getUser(query);
                        messageDto = new ImMessageDto();
                        messageDto.setUserID(userID);
                        messageDto.setUsername(user.getUsername());
                        messageDto.setAvatar(user.getAvatar());
                        messageDto.setMessageList(new LinkedList<>());
                        map.put(userID, messageDto);
                    }
                    messageDto.getMessageList().add(message);
                }
            });
            return new ArrayList<>(map.values());
        }
        return new LinkedList<>();
    }
}
