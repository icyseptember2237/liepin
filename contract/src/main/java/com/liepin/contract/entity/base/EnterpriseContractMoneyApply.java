package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_contract_money_apply")
public class EnterpriseContractMoneyApply {
    @TableId
    private Long id;
    private Long contractId;
    private Long userId;
    private Long applyNum;
    private String moneyUsage;
    private Long auditId;
    private String auditTime;
    private String status;
    private String createTime;
    private String dlt;
}
