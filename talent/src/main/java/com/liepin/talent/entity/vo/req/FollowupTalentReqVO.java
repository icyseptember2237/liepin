package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FollowupTalentReqVO {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "客户类型（直接/中介）")
    private String type;
    @ApiModelProperty(notes = "中介表id")
    private Long agencyId;
    @ApiModelProperty(notes = "联系电话")
    private String phone;
    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "聘用到期时间")
    private String hireEndtime;
    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;
    @ApiModelProperty(notes = "九大员")
    private String nineMember;
    @ApiModelProperty(notes = "任职类型（项目/资质）")
    private String hireType;
    @ApiModelProperty(notes = "三类证书(A/B/C)")
    private String threeCertificate;
    @ApiModelProperty(notes = "三类有效期")
    private String certificateDeadline;
    @ApiModelProperty(notes = "跟进备注")
    private String followupRemark;
    @ApiModelProperty(notes = "下次跟进时间")
    private String nextTime;
}
