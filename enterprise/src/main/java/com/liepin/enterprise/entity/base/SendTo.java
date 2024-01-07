package com.liepin.enterprise.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("send_to")
public class SendTo {
    private Long id;
    private String dept;
    private Long privateId;
    private String enterpriseName;
    private Long fromId;
    private Long toId;
    private String createTime;
    private String remark;
    private String auditStatus;
    private Long auditId;
    private String auditRemark;
    private String dlt;
}
