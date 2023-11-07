package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetNotContactList {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "省")
    private String province;
    @ApiModelProperty(notes = "市")
    private String city;
    @ApiModelProperty(notes = "区")
    private String county;
    @ApiModelProperty(notes = "企业法人")
    private String legalRepresentative;
    @ApiModelProperty(notes = "拉入时间")
    private String createTime;

}
