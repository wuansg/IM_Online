package xyz.silverspoon.service;

import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.dto.ImMessageDto;
import xyz.silverspoon.param.UserMessageParam;

import java.util.List;

public interface ImMessageService {
    List<ImMessage> listMessage(String uuid);

    List<ImMessage> listUserMessage(UserMessageParam userMessageParam);

    ImMessage saveMessage(ImMessage message);

    List<ImMessageDto> getUnreads(String uuid);
}
