package com.liepin.worklog_agency.entity.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImportAgencyResVO {
    @ApiModelProperty(notes = "导入数据量")
    private Long total;
    @ApiModelProperty(notes = "导入耗时(毫秒)")
    private Long milSeconds;
}
