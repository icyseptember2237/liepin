package com.liepin.enterprise.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditReqVO {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "通过/不通过", value = "PASS/FAIL")
    private String status;
}
