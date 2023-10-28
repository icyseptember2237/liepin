package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogProblemRes;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.mapper.LogProblemMapper;
import com.liepin.worklog_agency.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper,WorkLog> implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private LogProblemMapper logProblemMapper;

    @Override
    public Result<WorkLogRes> getWorkLog(String loginId) {
        WorkLogRespVo workLogRespVo = logMapper.getWorkLog(loginId);
        List<WorkLogProblem> workLogProblemList  = logMapper.getWorkLogProblem(loginId);
        System.out.println(workLogProblemList);
        workLogRespVo.setWorkLogProbList(workLogProblemList);

        List<WorkLogProblemRes> workLogProblemResList = new ArrayList<>();

        WorkLogRes workLogRes = new WorkLogRes();
        BeanUtils.copyProperties(workLogRespVo,workLogRes);
        workLogRes.setWorkLogProblemResList(workLogProblemResList);
        return Result.success(workLogRes);
    }

    @Override
    public Result insertWorkLog(WorkLogRespVo workLogRespVo) {
        WorkLog workLog = new WorkLog();
        String loginIdAsString = StpUtil.getLoginIdAsString();
        WorkLogRespVo workLog1 = logMapper.getWorkLog(loginIdAsString);

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
}
