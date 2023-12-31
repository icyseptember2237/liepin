package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetEnterpriseListVO {
    @ApiModelProperty(notes = "公海id")
    private Long id;
    @ApiModelProperty(notes = "单位名称")
    private String name;
    @ApiModelProperty(notes = "省名")
    private String province;
    @ApiModelProperty(notes = "城市名")
    private String city;
    @ApiModelProperty(notes = "区名")
    private String county;
    @ApiModelProperty(notes = "企业法人")
    private String legalRepresentative;
    @ApiModelProperty(notes = "联系人")
    private String contact;
    @ApiModelProperty(notes = "电话")
    private String phone;
    @ApiModelProperty(notes = "邮箱")
    private String email;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
    @ApiModelProperty(notes = "扔回原因")
    private String throwbackReason;
}
