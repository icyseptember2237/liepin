package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class GetTalentListReqVO {
    private Integer page;
    private Integer pageSize;
    @ApiModelProperty(notes = "人才姓名")
    private String name;
    @ApiModelProperty(notes = "联系电话")
    private String phone;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "客户类型")
    private String type;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;

}
