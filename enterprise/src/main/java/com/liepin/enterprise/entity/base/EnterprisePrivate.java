package com.liepin.enterprise.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_private")
public class EnterprisePrivate {
    private Long id;
    private Long userId;
    private Long infoId;
    private String createTime;
    private String status;
    private String sendStatus;
    private String throwback;
}
