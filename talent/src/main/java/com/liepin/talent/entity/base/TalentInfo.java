package com.liepin.talent.entity.base;



import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
* 人才信息表
* @TableName talent_info
*/
@Data
@TableName("talent_info")
public class TalentInfo implements Serializable {

    @TableId
    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "人才姓名")
    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ApiModelProperty(notes = "客户类型（直接/中介）")
    private String type;

    @ApiModelProperty(notes = "中介表id")
    private Long agencyId;

    @ApiModelProperty(notes = "联系电话")
    @ExcelProperty(value = "联系电话", index = 1)
    private String phone;

    @ApiModelProperty(notes = "身份证号")
    private String idNum;

    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;

    @ApiModelProperty(notes = "性别")
    @ExcelProperty(value = "性别", index = 2)
    private String sex;

    @ApiModelProperty(notes = "意向度")
    private String intention;

    @ApiModelProperty(notes = "证书类型")
    @ExcelProperty(value = "证书类型",index = 3)
    private String certificateType;

    @ApiModelProperty(notes = "证书专业")
    @ExcelProperty(value = "证书专业",index = 4)
    private String certificateMajor;

    @ApiModelProperty(notes = "聘用到期时间")
    private String hireEndtime;

    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;

    @ApiModelProperty(notes = "职称等级")
    private String professionLevel;

    @ApiModelProperty(notes = "九大员")
    private String nineMember;

    @ApiModelProperty(notes = "其他证书")
    private String otherCertificate;

    @ApiModelProperty(notes = "技工证")
    private String technicianCertificate;

    @ApiModelProperty(notes = "注册消防工程师")
    private String fireEngineer;

    @ApiModelProperty(notes = "任职类型（项目/资质）")
    private String hireType;

    @ApiModelProperty(notes = "三类证书(A/B/C)")
    private String threeCertificate;

    @ApiModelProperty(notes = "三类有效期")
    private String certificateDeadline;

    @ApiModelProperty(notes = "创建时间")
    private String createTime;

    @ApiModelProperty(notes = "是否位于私库")
    private String isPrivate;

    @ApiModelProperty(notes = "删除标志")
    private String dlt;

}
