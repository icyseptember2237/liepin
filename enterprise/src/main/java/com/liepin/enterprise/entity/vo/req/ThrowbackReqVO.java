package com.liepin.enterprise.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThrowbackReqVO {
    @ApiModelProperty(notes = "私海id")
    private Long id;
    @ApiModelProperty(notes = "扔回原因")
    private String throwbackReason;
    @ApiModelProperty(notes = "扔回备注")
    private String remark;
}
