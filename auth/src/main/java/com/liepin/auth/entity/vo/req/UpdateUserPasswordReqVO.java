package com.liepin.auth.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateUserPasswordReqVO {
    @ApiModelProperty(notes = "username")
    private String username;
    @ApiModelProperty(notes = "原密码")
    private String oldPassword;
    @ApiModelProperty(notes = "新密码")
    private String newPassword;
}
