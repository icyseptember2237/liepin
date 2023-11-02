package com.liepin.worklog_agency.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetAgencyReqVO {
    @ApiModelProperty(value = "中介名称")
    private String enterpriseName;
    @ApiModelProperty(value = "省份")
    private String province;
    @ApiModelProperty(value = "市区")
    private String city;
    @ApiModelProperty(value = "是否通过(PASS通过WAIT审核FAIL拒绝ALL全部)")
    private String auditStatus;
    private Integer page;
    private Integer pageSize;
}
