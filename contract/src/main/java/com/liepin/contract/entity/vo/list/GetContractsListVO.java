package com.liepin.contract.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetContractsListVO {
    private Long id;
    @ApiModelProperty(notes = "合同款项")
    private String priceType;
    @ApiModelProperty(notes = "合同单价")
    private BigDecimal contractPrice;
    @ApiModelProperty(notes = "聘用类型")
    private String hireType;
    @ApiModelProperty(notes = "聘用时间")
    private Long hireTime;
    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;
    @ApiModelProperty(notes = "三类证书")
    private String threeCertificate;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;
    @ApiModelProperty(notes = "出场要求")
    private String appearanceRequire;
    @ApiModelProperty(notes = "需求数量")
    private Integer requireNum;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "已匹配人数")
    private Long matchNum;
}
