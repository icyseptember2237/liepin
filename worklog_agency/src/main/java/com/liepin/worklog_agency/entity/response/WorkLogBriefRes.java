package com.liepin.worklog_agency.entity.response;

import lombok.Data;

@Data
public class WorkLogBriefRes {
    private String userName;
    private Long roleId;
    private Integer unfinishedProblemNum;
    private String updateTime;
}
