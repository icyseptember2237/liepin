package com.liepin.worklog_agency.mapper;

import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    WorkLogRespVo getWorkLog(String loginId);

    List<WorkLogProblem> getWorkLogProblem(String loginId);

    void insertWorkLog(WorkLog workLog);
    void insertWorkLogDetail(WorkLogDetail workLogDetail);
    void insertWorkLogProblem();
}
