package com.liepin.contract.entity.vo.list;

import com.liepin.contract.entity.base.ContractMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractMatchInfo {
    @ApiModelProperty(notes = "合同id")
    private Long contractId;
    @ApiModelProperty(notes = "需求id")
    private Long requireId;
    @ApiModelProperty(notes = "匹配人id")
    private Long userId;
    @ApiModelProperty(notes = "匹配人姓名")
    private String username;
    @ApiModelProperty(notes = "人才价格")
    private BigDecimal talentPrice;
    @ApiModelProperty(notes = "人才id")
    private Long talentId;
    @ApiModelProperty(notes = "人才姓名")
    private String talentName;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书类型")
    private String certificateMajor;
    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;
    @ApiModelProperty(notes = "九大员")
    private String nineMember;
    @ApiModelProperty(notes = "三类证书")
    private String threeCertificate;
    @ApiModelProperty(notes = "匹配时间")
    private String createTime;
}
