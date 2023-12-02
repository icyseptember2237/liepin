package com.liepin.auth.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginRespVO {
    @ApiModelProperty(notes = "用户id")
    private Long id;
    @ApiModelProperty(notes = "权限token")
    private String Authorization;
    @ApiModelProperty(notes = "姓名")
    private String name;
    @ApiModelProperty(notes = "用户名")
    private String username;
    @ApiModelProperty(notes = "角色")
    private String role;
    @ApiModelProperty(notes = "昨天是否提交工作日志")
    private Boolean commitWorkLog;
}
