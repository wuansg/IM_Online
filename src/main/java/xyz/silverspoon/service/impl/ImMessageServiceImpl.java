package xyz.silverspoon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.dto.ImMessageDto;
import xyz.silverspoon.dto.ImUserDto;
import xyz.silverspoon.param.UserMessageParam;
import xyz.silverspoon.repository.ImMessageRepository;
import xyz.silverspoon.repository.ImUserRepository;
import xyz.silverspoon.service.ImMessageService;
import xyz.silverspoon.utils.FileStorageUtils;
import xyz.silverspoon.utils.UUIDGenerator;
import xyz.silverspoon.utils.UUIDType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ImMessageServiceImpl implements ImMessageService {

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private FileStorageUtils fileStorageUtils;

    @Value(value = "${file.upload.path}")
    private String FILEUPLOADPATH;

    @Autowired
    private ImMessageRepository messageRepository;

    @Autowired
    private ImUserRepository userRepository;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    public ImMessageServiceImpl() {
    }

    @Override
    public List<ImMessage> listMessage(String uuid) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where(Constants.MESSAGE_SENDERID).is(uuid), Criteria.where(Constants.MESSAGE_RECEIVERID).is(uuid));
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, Constants.MESSAGE_TIME))
                .with(PageRequest.of(0, 10));
        return messageRepository.listMessages(query);
    }

    @Override
    public Page<ImMessage> listUserMessage(String uuid, int pageNum, int pageSize) {
        // TODO 添加单个用户的聊天记录获取
        return null;
    }

    @Override
    public ImMessage saveMessage(ImMessage message) {
        message.setUUID(uuidGenerator.generateUUID(UUIDType.IM_MESSAGE));
        message = messageRepository.save(message);
        return message;
    }

    @Override
    public List<ImMessageDto> getUnreads(String uuid) {
        Query query = new Query(Criteria.where(Constants.MESSAGE_RECEIVERID).is(uuid).and(Constants.MESSAGE_STATUS).is(0));
        List<ImMessage> messages = messageRepository.listMessages(query);
        if (!CollectionUtils.isEmpty(messages)) {
            Map<String, ImMessageDto> map = new HashMap<>();
            messages.forEach(o -> {
                String userID = o.getSenderID().equals(uuid) ? o.getReceiverID() : o.getSenderID();
                ImMessageDto messageDto = map.get(userID);
                if (messageDto == null) {
                    Query query2 = new Query(Criteria.where(Constants.IM_UUID).is(userID));
                    ImUser user = userRepository.getUser(query2);
                    messageDto = new ImMessageDto();
                    messageDto.setUserID(userID);
                    messageDto.setUsername(user.getUsername());
                    messageDto.setAvatar(user.getAvatar());
                    messageDto.setMessageList(new LinkedList<>());
                    map.put(userID, messageDto);
                }
                messageDto.getMessageList().add(o);
            });
            return new ArrayList<>(map.values());
        }
        return new LinkedList<>();
    }

    @Override
    public String uploadFile(MultipartFile file, UUIDType type) {
        return fileStorageUtils.saveFile(file, type);
    }

    @Override
    public void updateStates(String receiverID, String senderID) {
        Query query = new Query(Criteria.where(Constants.MESSAGE_RECEIVERID).is(receiverID)
                .and(Constants.MESSAGE_SENDERID).is(senderID));
        Update update = new Update().set("status", 1);
        messageRepository.update(query, update);
    }

    @Override
    public Resource downloadFile(String filepath) {
        return fileStorageUtils.loadFileAsResource(filepath);
    }
}
