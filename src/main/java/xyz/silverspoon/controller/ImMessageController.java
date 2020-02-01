package xyz.silverspoon.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.component.ImCommonResult;
import xyz.silverspoon.dto.ImMessageDto;
import xyz.silverspoon.param.UserMessageParam;
import xyz.silverspoon.service.ImMessageService;
import xyz.silverspoon.service.ImUserService;

import java.util.*;
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
//        System.out.println(uuid);
        List<ImMessage> messages = messageService.listMessage(uuid);
//        messages.forEach(System.out::println);
        Set<String> uuids = messages.stream().map(ImMessage::getSenderID).collect(Collectors.toSet());
        uuids.addAll(messages.stream().map(ImMessage::getReceiverID).collect(Collectors.toSet()));
        uuids.remove(uuid);
//        uuids.forEach(System.out::println);

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
//        messages.forEach(message -> {
//            String id = message.getSenderID().equals(uuid) ? message.getReceiverID() : message.getSenderID();
//            messageDtos.get(id).getMessageList().set(0, message);
//        });
//        messageDtos.values().forEach(System.out::println);
        return ImCommonResult.success(new ArrayList<>(messageDtos.values()));
    }

    @RequestMapping(value = "/message/{userUUID}", method = RequestMethod.GET)
    public ImCommonResult<List<ImMessage>> getMessageByUser(@RequestBody UserMessageParam param) {
        List<ImMessage> messages = messageService.listUserMessage(param);
        return ImCommonResult.success(messages);
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public ImCommonResult<ImMessage> addMessage(@RequestBody ImMessage message) {
        message.setTime(new Date());
        message = messageService.saveMessage(message);
        return ImCommonResult.success(message);
    }

    @RequestMapping(value = "/message/unread/{uuid}", method = RequestMethod.GET)
    public ImCommonResult<List<ImMessageDto>> getUnreads(@PathVariable String uuid) {
        List<ImMessageDto> messageDtos = messageService.getUnreads(uuid);
        return ImCommonResult.success(messageDtos);
    }
}
