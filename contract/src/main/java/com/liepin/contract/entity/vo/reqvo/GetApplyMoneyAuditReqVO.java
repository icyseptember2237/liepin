package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetApplyMoneyAuditReqVO {
    private Long page;
    private Long pageSize;
    @ApiModelProperty(notes = "PASS WAIT FAIL")
    private String status;
}
