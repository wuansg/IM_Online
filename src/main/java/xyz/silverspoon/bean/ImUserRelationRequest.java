package xyz.silverspoon.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ImUserRelationRequest {
    private String UUID;
    private String requestID;
    private String acceptID;
    private Integer status;
    private Long createTime;
    private Long modifyTime;
}
