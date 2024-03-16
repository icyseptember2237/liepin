package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_contract")
public class EnterpriseContract {
    private Long id;
    private Long privateId;
    private Long userId;
    private String priceType;
    private Long totalPrice;
    private Long profit;
    private Long registeredAmount;
    private Long amountOnContract;
    private Long usedAmount;
    private Long totalRequireNum;
    private String remark;
    private String uploadContract;
    private String contractNumber;
    private String contractLink;
    private String status;
    private String createTime;
    private String dlt;
}
