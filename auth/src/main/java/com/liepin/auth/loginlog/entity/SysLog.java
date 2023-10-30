package com.liepin.auth.loginlog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_log")
public class SysLog {
    private Long id;
    private String username;
    private String res;
    private String msg;
    private String ip;
    private String time;
}
