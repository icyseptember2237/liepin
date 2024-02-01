package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("contract_match")
public class ContractMatch {
    private Long id;
    private Long contractId;
    private Long talentId;
    private Long talentPrice;
    private Long servicePrice;
    private Long userId;
    private String createTime;
    private String dlt;
}
