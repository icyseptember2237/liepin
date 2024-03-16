package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditApplyReqVO {
    @ApiModelProperty(notes = "合同id 加锁用，必传")
    private Long contractId;
    @ApiModelProperty(notes = "申请id")
    private Long applyId;
    @ApiModelProperty(notes = "审核结果")
    private String status;
}
