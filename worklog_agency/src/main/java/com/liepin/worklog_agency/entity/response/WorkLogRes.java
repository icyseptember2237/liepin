package com.liepin.worklog_agency.entity.response;

import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import lombok.Data;

import java.util.List;
@Data
public class WorkLogRes {
    private Integer id;
    private Integer intentionalCustomer;
    private Integer logId;
    private Integer phoneNum;
    private Integer wechatNum;
    private List<WorkLogProblemRes> workLogProbList;
    private String workPlan;
    private String workSituation;
    private String workSummary;
}
