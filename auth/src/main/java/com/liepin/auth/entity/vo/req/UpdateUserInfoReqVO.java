package com.liepin.auth.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateUserInfoReqVO extends CreateUserReqVO{
    @ApiModelProperty(notes = "用户id")
    private Long id;
}
