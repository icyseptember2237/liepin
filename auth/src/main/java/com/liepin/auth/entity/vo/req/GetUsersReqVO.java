package com.liepin.auth.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetUsersReqVO {
    @ApiModelProperty(notes = "用户id")
    private Long id;
    @ApiModelProperty(notes = "账号")
    private String username;
    @ApiModelProperty(value = "MANAGER",notes = "目前三种：MANAGER TALENT ENTERPRISE")
    private String role;
    @ApiModelProperty(notes = "姓名")
    private String name;
    @ApiModelProperty(value = "YES",notes = "帐号状态 NO YES")
    private String status;
    private Integer page;
    private Integer pageSize;

}
