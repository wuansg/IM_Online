package xyz.silverspoon.param;

import lombok.Data;

@Data
public class UserMessageParam {
    private String SenderID;
    private String ReceiverID;
    private int page;
    private int size;
}
