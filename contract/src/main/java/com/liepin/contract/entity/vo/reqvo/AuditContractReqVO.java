package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditContractReqVO {
    @ApiModelProperty(notes = "合同id")
    private Long contractId;
    @ApiModelProperty(notes = "审核状态 PASS FAIL")
    private String status;
    @ApiModelProperty(notes = "备注")
    private String remark;
}
