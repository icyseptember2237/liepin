package com.liepin.enterprise.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SendToReqVO {
    @ApiModelProperty(notes = "推向员工id")
    private Long userId;
    @ApiModelProperty(notes = "私库id")
    private Long privateId;
    @ApiModelProperty(notes = "内推备注")
    private String remark;
}
