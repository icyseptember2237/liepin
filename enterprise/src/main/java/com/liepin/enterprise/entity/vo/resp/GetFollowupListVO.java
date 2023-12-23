package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetFollowupListVO {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "公司名称")
    private String name;
    @ApiModelProperty(notes = "省")
    private String province;
    @ApiModelProperty(notes = "市")
    private String city;
    @ApiModelProperty(notes = "区")
    private String county;
    @ApiModelProperty(notes = "法人代表")
    private String legalRepresentative;
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "失联天数")
    private Integer lostContactDays;
}
