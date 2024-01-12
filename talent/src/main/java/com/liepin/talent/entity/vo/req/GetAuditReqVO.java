package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetAuditReqVO {
    @ApiModelProperty(notes = "开始时间")
    private String startTime;
    @ApiModelProperty(notes = "结束时间")
    private String endTime;
    @ApiModelProperty(notes = "审核状态",value = "WAIT/PASS/FAIL")
    private String auditStatus;
    private Integer page;
    private Integer pageSize;
}
