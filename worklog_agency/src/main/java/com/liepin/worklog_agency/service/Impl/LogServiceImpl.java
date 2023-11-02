package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogServiceImpl extends ServiceImpl<LogMapper,WorkLog> implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public Result<WorkLogRes> getWorkLog(String loginId) {
        WorkLogRes workLogRes = new WorkLogRes();
        workLogRes = logMapper.getWorkLogRes(loginId);
        workLogRes.setLogId(StpUtil.getLoginIdAsInt());
        workLogRes.setWorkLogProbList(logMapper.getWorkLogProblemList(loginId,TimeUtil.getToday()));
        return Result.success(workLogRes);

    }

    @Override
    public Result insertWorkLog(WorkLogRespVo workLogRespVo) {
        WorkLog workLog = new WorkLog();
        String loginIdAsString = StpUtil.getLoginIdAsString();
        String today = TimeUtil.getToday();
        WorkLogRespVo workLog1 = logMapper.getWorkLog(loginIdAsString,today);

        if (workLog1!=null){
            Long id = workLog1.getId();
            workLog.setId(id);
        }
        workLog.setUserId(StpUtil.getLoginIdAsLong());
        workLog.setCreateTime(TimeUtil.getNowWithMin());
        workLog.setUpdateTime(TimeUtil.getNowWithMin());
        saveOrUpdate(workLog);
        return Result.success();
    }

    @Override
    public Result<WorkLogRes> getWorkLogByLogId(String logId) {

        WorkLogRes workLogRes = logMapper.getWorkLogResByLogId(logId);
        workLogRes.setWorkLogProbList(logMapper.getWorkLogProblemListById(logId));

        return Result.success(workLogRes);
    }
}
