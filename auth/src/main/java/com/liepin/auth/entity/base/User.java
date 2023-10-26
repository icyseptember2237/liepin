package com.liepin.auth.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {
    @TableId
    private Long id;
    private String username;
    private String phone;
    private String password;
    private Long roleId;
    private String name;
    private Integer age;
    private Integer sex;
    private String remark;
    private String status;
    private String createTime;
    private Long createId;
    private String updateTime;
    private Long updateId;
    private String dlt;
}
