package com.liepin.worklog_agency.entity.response;

import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class WorkLogRes {
    @ApiModelProperty(notes = "日志id")
    private Integer id;
    @ApiModelProperty(notes = "意向用户数量")
    private Integer intentionalCustomer;
    @ApiModelProperty(notes = "登录用户id")
    private Integer logId;
    @ApiModelProperty(notes = "电话量")
    private Integer phoneNum;
    @ApiModelProperty(notes = "微信量")
    private Integer wechatNum;
    @ApiModelProperty(notes = "未完成事项List")
    private List<WorkLogProblemRes> workLogProbList;
    @ApiModelProperty(notes = "明日工作计划")
    private String workPlan;
    @ApiModelProperty(notes = "今日工作完成情况")
    private String workSituation;
    @ApiModelProperty(notes = "今日工作总结")
    private String workSummary;
}
