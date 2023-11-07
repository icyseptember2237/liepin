package com.liepin.worklog_agency.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AgencyNameAndId {
    @ApiModelProperty(value = "中介id")
    private Long id;
    @ApiModelProperty(value = "中介名称")
    private String enterpriseName;
}
