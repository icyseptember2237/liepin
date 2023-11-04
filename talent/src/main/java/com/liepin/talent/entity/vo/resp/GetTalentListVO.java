package com.liepin.talent.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetTalentListVO {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "姓名")
    private String name;
    @ApiModelProperty(notes = "客户类型")
    private String type;
    @ApiModelProperty(notes = "中介名称")
    private String agencyName;
    @ApiModelProperty(notes = "电话")
    private String phone;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "任职类型")
    private String hireType;
}
