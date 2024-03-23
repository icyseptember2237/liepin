package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("talent_contract_money_apply")
public class TalentContractMoneyApply {
    @TableId
    private Long id;
    private Long matchId;
    private Long contractId;
    private Long requireId;
    private Long userId;
    private Long applyNum;
    private String moneyUsage;
    private Long auditId;
    private String auditTime;
    private String status;
    private String createTime;
    private String dlt;
}
