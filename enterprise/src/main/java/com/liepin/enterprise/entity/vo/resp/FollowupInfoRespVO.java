package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FollowupInfoRespVO {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "单位名称")
    private String name;
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "注册资金")
    private String registeredCapital;
    @ApiModelProperty(notes = "省")
    private String province;
    @ApiModelProperty(notes = "市")
    private String city;
    @ApiModelProperty(notes = "区")
    private String county;
    @ApiModelProperty(notes = "企业邮箱")
    private String email;
    @ApiModelProperty(notes = "资质等级")
    private String qualification;
    @ApiModelProperty(notes = "法人代表")
    private String legalRepresentative;
    @ApiModelProperty(notes = "联系人")
    private String contact;
    @ApiModelProperty(notes = "联系电话")
    private String phone;
    @ApiModelProperty(notes = "地址")
    private String address;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "跟进记录")
    private List<FollowupHistory> list;
}
