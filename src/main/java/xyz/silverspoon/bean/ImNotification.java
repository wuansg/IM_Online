package xyz.silverspoon.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ImNotification {
    private String UUID;
    private String userID;
    private int type;
    private String content;
    private int status;
    private Date time;
}
