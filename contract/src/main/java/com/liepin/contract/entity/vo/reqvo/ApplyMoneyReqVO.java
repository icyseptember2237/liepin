package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplyMoneyReqVO {
    @ApiModelProperty(notes = "合同id")
    private Long contractId;
    @ApiModelProperty(notes = "申请金额")
    private BigDecimal money;
    @ApiModelProperty(notes = "用途")
    private String usage;
}
