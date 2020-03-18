package xyz.silverspoon.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.silverspoon.Constants;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.dto.ImMessageDto;
import xyz.silverspoon.param.UserMessageParam;
import xyz.silverspoon.service.ImMessageService;
import xyz.silverspoon.service.ImUserService;
import xyz.silverspoon.utils.UUIDType;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//@CrossOrigin(origins = {"http://localhost:8081", "http://localhost"}, allowCredentials = "true")
@RestController
@RequestMapping(value = "/api")
public class ImMessageController {
    @Autowired
    private ImMessageService messageService;
    @Autowired
    private ImUserService userService;

    @RequestMapping(value = "/recent/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<List<ImMessageDto>> getRecentMessage(@PathVariable String uuid) {
        List<ImMessage> messages = messageService.listMessage(uuid);
        Set<String> uuids = messages.stream().map(ImMessage::getSenderID).collect(Collectors.toSet());
        uuids.addAll(messages.stream().map(ImMessage::getReceiverID).collect(Collectors.toSet()));
        uuids.remove(uuid);

        Map<String, ImMessageDto> messageDtos = uuids.stream().map(o -> userService.getUserByUUID(o)).collect(Collectors.toMap(ImUser::getUUID, o -> {
            ImMessageDto messageDto = new ImMessageDto();
            messageDto.setUserID(o.getUUID());
            messageDto.setUsername(o.getUsername());
            messageDto.setAvatar(o.getAvatar());
            return messageDto;
        }));
        for (int i = messages.size()-1; i >= 0 ; i--) {
            ImMessage message = messages.get(i);
            String id = message.getSenderID().equals(uuid) ? message.getReceiverID() : message.getSenderID();
            messageDtos.get(id).getMessageList().add(message);
        }
        return ImCommonResult.success(new ArrayList<>(messageDtos.values()));
    }

    @RequestMapping(value = "/message/{userUUID}", method = RequestMethod.GET)
    public ImCommonResult<Page<ImMessage>> getMessageByUser(@PathVariable String userUUID,
                                                            @RequestParam int pageNum,
                                                            @RequestParam int pageSize) {
        Page<ImMessage> messages = messageService.listUserMessage(userUUID, pageNum, pageSize);
        return ImCommonResult.success(messages);
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public ImCommonResult<ImMessage> addMessage(@RequestBody ImMessage message) {
        message.setTime(System.currentTimeMillis());
        message = messageService.saveMessage(message);
        return ImCommonResult.success(message);
    }

    @RequestMapping(value = "/message/unread/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<List<ImMessageDto>> getUnreads(@PathVariable String uuid) {
        List<ImMessageDto> messageDtos = messageService.getUnreads(uuid);
        return ImCommonResult.success(messageDtos);
    }

    @RequestMapping(value = "/message/mark", method = RequestMethod.POST)
    public ImCommonResult<String> mark(@RequestBody ImMessage message) {
        messageService.updateStates(message.getReceiverID(), message.getSenderID());
        return ImCommonResult.success("success");
    }

    @RequestMapping(value = "/message/pic", method = RequestMethod.POST)
    public ImCommonResult<String> uploadPic(MultipartFile file) {
        String filename = messageService.uploadFile(file, UUIDType.IM_PIC);
        if ("".equals(filename)) {
            return ImCommonResult.error(501, Constants.FILE_EXCEPTION);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ImCommonResult.success(filename);
    }

    @RequestMapping(value = "/message/file", method = RequestMethod.POST)
    public ImCommonResult<String> uploadFile(MultipartFile file) {
        String filename = messageService.uploadFile(file, UUIDType.IM_FILE);
        if ("".equals(filename)) {
            return ImCommonResult.error(501, Constants.FILE_EXCEPTION);
        }
        return ImCommonResult.success(filename);
    }
}
