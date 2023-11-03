package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImportEnterpriseRespVO {
    @ApiModelProperty(notes = "导入数据数量")
    private Long total;
    @ApiModelProperty(notes = "耗时（毫秒）")
    private Long milSeconds;
}
