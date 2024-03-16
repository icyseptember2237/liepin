package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetSelfContractReqVO {
    private Integer page;
    private Integer pageSize;
    @ApiModelProperty(notes = "READY可匹配 MATCHING匹配中 UNFINISH未完结 FINISH已完结")
    private String status;
}
