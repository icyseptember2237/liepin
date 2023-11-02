package com.liepin.worklog_agency.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class UpdateAgencyReqVO {
    @ApiModelProperty(notes = "id")
    private String id;
    @ApiModelProperty(notes = "企业名称")
    private String enterpriseName;
    @ApiModelProperty(notes = "企业法人")
    private String person;
    @ApiModelProperty(notes = "省")
    private String province;
    @ApiModelProperty(notes = "市")
    private String city;
    @ApiModelProperty(notes = "详细地址")
    private String detailAddr;
    @ApiModelProperty(notes = "联系电话")
    private String tel;

}
