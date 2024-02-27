package com.liepin.contract.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewContractRequireListVO {
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;
    @ApiModelProperty(notes = "九大员")
    private String nineMember;
    @ApiModelProperty(notes = "三类证书 ABC")
    private String threeCertificate;
    @ApiModelProperty(notes = "任职类型 项目/资质")
    private String hireType;
    @ApiModelProperty(notes = "聘用周期")
    private Integer hireTime;
    @ApiModelProperty(notes = "出场要求 到场/不到场")
    private String appearanceRequire;
    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;
    @ApiModelProperty(notes = "需求价格")
    private BigDecimal price;
    @ApiModelProperty(notes = "其他费用")
    private BigDecimal otherPrice;
    @ApiModelProperty(notes = "需求本数")
    private Long requireNum;

}
