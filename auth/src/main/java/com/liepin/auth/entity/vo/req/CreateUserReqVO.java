package com.liepin.auth.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateUserReqVO {
    @ApiModelProperty(notes = "用户名")
    private String username;
    @ApiModelProperty(notes = "电话")
    private String phone;
    @ApiModelProperty(notes = "角色id")
    private Long role;
    @ApiModelProperty(notes = "姓名")
    private String name;
    @ApiModelProperty(notes = "年龄")
    private Integer age;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "备注")
    private String remark;

}
