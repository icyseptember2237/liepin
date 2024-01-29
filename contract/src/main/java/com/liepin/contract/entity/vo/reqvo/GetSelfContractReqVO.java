package com.liepin.contract.entity.vo.reqvo;

import lombok.Data;

@Data
public class GetSelfContractReqVO {
    private Integer page;
    private Integer pageSize;
    private String status;
}
