package xyz.silverspoon.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ImUser implements Serializable {
    private String UUID;
    private String username;
    private String password;
    private String avatar;
    private String signature;
    private Date createTime;
    private Integer sex;
    private String email;

}
