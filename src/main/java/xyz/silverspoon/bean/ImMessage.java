package xyz.silverspoon.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ImMessage {
    private String UUID;
    private String SenderID;
    private String ReceiverID;
    private Integer type;
    private String content;
    private Integer status;
    private Date time;
}
