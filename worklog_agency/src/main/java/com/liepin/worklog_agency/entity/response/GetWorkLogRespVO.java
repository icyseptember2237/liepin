package com.liepin.worklog_agency.entity.response;

import lombok.Data;

import java.util.List;
@Data
public class GetWorkLogRespVO {
    private List<WorkLogBriefRes> list;
    private Long total;
}
