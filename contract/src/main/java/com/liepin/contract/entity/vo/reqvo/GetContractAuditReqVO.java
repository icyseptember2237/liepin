package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetContractAuditReqVO {
    private Long page;
    private Long pageSize;
    @ApiModelProperty(notes = "审核状态 WAIT待审核 PASS审核通过 FAIL未通过 FINANCEWAIT财务待审核 FINANCEPASS财务审核通过 FINANCEFAIL财务审核不通过")
    private String status;
}
