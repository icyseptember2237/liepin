package com.liepin.worklog_agency.entity.base;

import com.liepin.common.config.exception.ExceptionsEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.File;

@Data
public class AddAgencyReqVO {

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
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "认证图片")
    private String fileLink;
}
