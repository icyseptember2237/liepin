package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetNotContactReqVO {
    @ApiModelProperty(notes = "意向度")
    private String intention;
    @ApiModelProperty(notes = "人才名称")
    private String name;
    @ApiModelProperty(notes = "联系电话")
    private String phone;
    private Integer page;
    private Integer pageSize;
}
