package com.liepin.talent.entity.vo.req;

import lombok.Data;

@Data
public class AuditSendReqVO {
    private Long id;
    private String status;
    private String auditRemark;
}
