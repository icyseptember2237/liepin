package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditMoneyReqVO {
    @ApiModelProperty(notes = "合同id 同步加锁用的，必须传")
    private Long contractId;
    @ApiModelProperty(notes = "认款id")
    private Long registerId;
    @ApiModelProperty(notes = "状态")
    private String status;
    @ApiModelProperty(notes = "备注")
    private String remark;
}
