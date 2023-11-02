package com.liepin.worklog_agency.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetWorkLogReqVO {
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value ="人名")
    private String name;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "部门 MANAGE TALENT ENTERPRISE ALL")
    private String role;
    @ApiModelProperty(value = "是否解决YES or NO or ALL")
    private String solved;
    private Integer page;
    private Integer pageSize;

}
