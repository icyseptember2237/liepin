package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogProblemRes;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<WorkLog> {

    WorkLogRespVo getWorkLog(Long loginId,String createTime);

    List<WorkLogProblem> getWorkLogProblem(Long loginId);

    void insertWorkLog(WorkLog workLog);

    WorkLogRes getWorkLogRes(Long loginId,String dayTime);

    List<WorkLogProblemRes> getWorkLogProblemList(Long loginId, String dayTime);

    WorkLogRes getWorkLogResByLogId(Long logId);

    List<WorkLogProblemRes> getWorkLogProblemListById(Long logId);
}
