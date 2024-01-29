package com.liepin.contract.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetContractsByPrivateIdListVO {
    @ApiModelProperty(notes = "合同id")
    private Long id;
    @ApiModelProperty(notes = "用户id")
    private Long userId;
    @ApiModelProperty(notes = "用户姓名")
    private String name;
    @ApiModelProperty(notes = "合同单价")
    private BigDecimal contractPrice;
    @ApiModelProperty(notes = "合同款项")
    private String priceType;
    @ApiModelProperty(notes = "合同状态 READY可匹配 WAIT审核中")
    private String status;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
}
