package com.liepin.talent.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetAllFollowupListVO {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "人才名称")
    private String name;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "电话")
    private String phone;
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "职称")
    private String professionCertificate;
    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;
    @ApiModelProperty(notes = "三类证书")
    private String threeCertificate;
    @ApiModelProperty(notes = "九大员")
    private String nineMember;
    @ApiModelProperty(notes = "社保")
    private String socialInsurance;
    @ApiModelProperty(notes = "失联天数")
    private Integer lostContactDays;
}
