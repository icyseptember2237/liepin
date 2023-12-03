package com.liepin.worklog_agency.entity.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkLogBriefRes {
    @ApiModelProperty(value = "当前日志id")
    private Long logId;
    @ApiModelProperty(value = "账户(username)")
    private String userName;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "员工id")
    private Long id;
    @ApiModelProperty(value = "部门")
    private String role;
    @ApiModelProperty(value = "电话数量")
    private Integer phoneNum;
    @ApiModelProperty(value = "微信数量")
    private Integer wechatNum;
    @ApiModelProperty(value = "意向用户数量")
    private Integer IntentionalCustomer;
    @ApiModelProperty(value = "未完成数量")
    private Integer unfinishedProblemNum;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
}
