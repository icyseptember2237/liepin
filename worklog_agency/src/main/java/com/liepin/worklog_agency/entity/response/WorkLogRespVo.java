package com.liepin.worklog_agency.entity.response;

import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import lombok.Data;

import java.util.List;
@Data
public class WorkLogRespVo extends WorkLogDetail {
    private List<WorkLogProblem> WorkLogProbList ;
}
