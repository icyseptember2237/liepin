package com.liepin.enterprise.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetNotContactReqVO {
    @ApiModelProperty(notes = "省")
    private String province;
    @ApiModelProperty(notes = "市")
    private String city;
    @ApiModelProperty(notes = "区")
    private String county;
    @ApiModelProperty(notes = "单位名称")
    private String name;
    @ApiModelProperty(notes = "联系电话")
    private String phone;
    @ApiModelProperty(notes = "联系人")
    private String contact;
    private Integer page;
    private Integer pageSize;
}
