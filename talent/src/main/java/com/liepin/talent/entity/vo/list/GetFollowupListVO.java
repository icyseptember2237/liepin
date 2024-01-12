package com.liepin.talent.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetFollowupListVO {
    @ApiModelProperty(notes = "私库id")
    private Long id;
    @ApiModelProperty(notes = "人才名称")
    private String name;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "电话")
    private String phone;
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "失联天数")
    private Integer lostContactDays;
}
