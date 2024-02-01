package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("contract_audit_history")
public class ContractAuditHistory {
    private Long id;
    private Long contractId;
    private Long userId;
    private String status;
    private Long auditId;
    private String auditTime;
    private String remark;
    private String dlt;
}
