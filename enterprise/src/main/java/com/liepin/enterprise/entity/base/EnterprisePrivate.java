package com.liepin.enterprise.entity.base;

import lombok.Data;

@Data
public class EnterprisePrivate {
    private Long id;
    private Long userId;
    private Long infoId;
    private Long followUpId;
    private String createTime;
    private String status;
    private String throwback;
    private String dlt;
}
