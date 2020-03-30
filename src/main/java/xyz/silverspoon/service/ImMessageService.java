package xyz.silverspoon.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import xyz.silverspoon.bean.ImMessage;
import xyz.silverspoon.dto.ImMessageDto;
import xyz.silverspoon.param.UserMessageParam;
import xyz.silverspoon.utils.UUIDType;

import java.util.List;

public interface ImMessageService {
    List<ImMessage> listMessage(String uuid);

    Page<ImMessage> listUserMessage(String uuid, int pageNum, int pageSize);

    ImMessage saveMessage(ImMessage message);

    List<ImMessageDto> getUnreads(String uuid);

    String uploadFile(MultipartFile file, UUIDType type);

    void updateStates(String receiverID, String senderID);

    Resource downloadFile(String filepath);
}
