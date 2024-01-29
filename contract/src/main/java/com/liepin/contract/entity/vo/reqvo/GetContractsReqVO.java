package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetContractsReqVO {
    private Integer page;
    private Integer pageSize;
    @ApiModelProperty(notes = "单位私库id")
    private Long privateId;
    @ApiModelProperty(notes = "单位部员工id")
    private Long userId;
}
