package com.liepin.auth.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetUsersListVO {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "用户名")
    private String username;
    @ApiModelProperty(notes = "电话")
    private String phone;
    @ApiModelProperty(notes = "角色名")
    private String roleName;
    @ApiModelProperty(notes = "角色代码")
    private String roleCode;
    @ApiModelProperty(notes = "姓名")
    private String name;
    @ApiModelProperty(notes = "年龄")
    private Integer age;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(value = "YES",notes = "账号状态")
    private String status;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
    @ApiModelProperty(notes = "创建人")
    private String createUser;
    @ApiModelProperty(notes = "更新时间")
    private String updateTime;
    @ApiModelProperty(notes = "更新人")
    private String updateUser;
}
