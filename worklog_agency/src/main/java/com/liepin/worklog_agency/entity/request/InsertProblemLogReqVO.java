package com.liepin.worklog_agency.entity.request;

import lombok.Data;

@Data
public class InsertProblemLogReqVO {
    private String problemInfo;
    private String solved;
    private String solution;
    private String contact;
    private String dlt;
}
