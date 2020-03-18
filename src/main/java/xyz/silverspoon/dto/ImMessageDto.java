package xyz.silverspoon.dto;

import lombok.Data;
import xyz.silverspoon.bean.ImMessage;

import java.util.LinkedList;
import java.util.List;

@Data
public class ImMessageDto {
    private String userID;
    private String username;
    private String avatar;
    private int count;

    private List<ImMessage> messageList = new LinkedList<>();
}
