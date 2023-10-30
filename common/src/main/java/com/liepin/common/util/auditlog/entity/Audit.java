package com.liepin.common.util.auditlog.entity;

import lombok.Data;

@Data
public class Audit {
    private Long id;
    private String tableName;
    private Long tableId;
    private String auditStatus;
    private String auditTime;
    private Long auditId;
    private String remark;
}
