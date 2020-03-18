package xyz.silverspoon.dto;

import lombok.Data;
import xyz.silverspoon.bean.ImUser;
import xyz.silverspoon.bean.ImUserRelationRequest;

import java.util.Date;

@Data
public class ImRequestDto {
    private String UUID;
    private ImUser requestUser;
    private Integer status;
    private Long createTime;
}
