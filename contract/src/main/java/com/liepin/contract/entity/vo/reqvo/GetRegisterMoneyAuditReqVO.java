package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetRegisterMoneyAuditReqVO {
    private Long page;
    private Long pageSize;
    @ApiModelProperty(notes = "WAIT PASS FAIL")
    private String status;
}
