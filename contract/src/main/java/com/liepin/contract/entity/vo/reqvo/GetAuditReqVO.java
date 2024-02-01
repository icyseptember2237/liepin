package com.liepin.contract.entity.vo.reqvo;

import lombok.Data;

@Data
public class GetAuditReqVO {
    private Long page;
    private Long pageSize;
    private String status;
}
