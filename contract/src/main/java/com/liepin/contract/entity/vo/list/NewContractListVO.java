package com.liepin.contract.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewContractListVO {
    @ApiModelProperty(notes = "单位私库id")
    private Long privateId;
    @ApiModelProperty(notes = "合同款项 全额/预付")
    private String priceType;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "三类证书")
    private String threeCertificate;
    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;
    @ApiModelProperty(notes = "聘用周期")
    private Integer hireTime;
    @ApiModelProperty(notes = "需求本数")
    private Integer requireNum;
    @ApiModelProperty(notes = "合同单价")
    private BigDecimal contractPrice;
    @ApiModelProperty(notes = "任职类型")
    private String hireType;
    @ApiModelProperty(notes = "出场要求")
    private String appearanceRequire;
    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;
    @ApiModelProperty(notes = "备注")
    private String remark;
}
