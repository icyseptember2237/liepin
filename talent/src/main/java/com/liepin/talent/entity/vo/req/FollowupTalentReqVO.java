package com.liepin.talent.entity.vo.req;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FollowupTalentReqVO {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "人才姓名")
    private String name;
    @ApiModelProperty(notes = "客户类型（直接/中介）")
    private String type;
    @ApiModelProperty(notes = "中介表id")
    private Long agencyId;
    @ApiModelProperty(notes = "联系电话")
    private String phone;
    @ApiModelProperty(notes = "身份证号")
    private String idNum;
    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "证书类型1")
    @ExcelProperty(value = "证书类型1",index = 3)
    private String certificateType1;

    @ApiModelProperty(notes = "证书专业/等级1")
    @ExcelProperty(value = "证书专业1",index = 4)
    private String certificateMajor1;

    @ApiModelProperty(notes = "证书类型2")
    @ExcelProperty(value = "证书类型2",index = 5)
    private String certificateType2;

    @ApiModelProperty(notes = "证书专业/等级2")
    @ExcelProperty(value = "证书专业2",index = 6)
    private String certificateMajor2;

    @ApiModelProperty(notes = "证书类型3")
    @ExcelProperty(value = "证书类型3",index = 7)
    private String certificateType3;

    @ApiModelProperty(notes = "证书专业/等级3")
    @ExcelProperty(value = "证书专业3",index = 8)
    private String certificateMajor3;
    @ApiModelProperty(notes = "聘用到期时间")
    private String hireEndtime;
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
