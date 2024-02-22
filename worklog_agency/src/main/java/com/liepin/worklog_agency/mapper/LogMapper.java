package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogProblemRes;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<WorkLog> {

    WorkLogRespVo getWorkLog(@Param("loginId") Long loginId,@Param("createTime") String createTime);

    List<WorkLogProblem> getWorkLogProblem(@Param("loginId") Long loginId);

    void insertWorkLog(@Param("workLog") WorkLog workLog);

    WorkLogRes getWorkLogRes(@Param("loginId") Long loginId,@Param("dayTime") String dayTime);

    List<WorkLogProblemRes> getWorkLogProblemList(@Param("loginId") Long loginId,@Param("dayTime") String dayTime);

    WorkLogRes getWorkLogResByLogId(@Param("logId")Long logId);

    List<WorkLogProblemRes> getWorkLogProblemListById(@Param("logId") Long logId);
}
