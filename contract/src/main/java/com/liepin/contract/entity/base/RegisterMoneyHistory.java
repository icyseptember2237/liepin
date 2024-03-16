package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("register_money_history")
public class RegisterMoneyHistory {
    private Long id;
    private Long contractId;
    private Long userId;
    private Long amount;
    private Long restFromTotal;
    private String status;
    private Long auditId;
    private String remark;
    private String auditTime;
    private String createTime;
    private String dlt;
}
