package com.liepin.auth.entity.vo.resp;

import lombok.Data;

@Data
public class GetUsersListVO {
    private Long id;
    private String username;
    private String phone;
    private String password;
    private String roleName;
    private String roleCode;
    private String name;
    private Integer age;
    private String sex;
    private String remark;
    private String status;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
}
