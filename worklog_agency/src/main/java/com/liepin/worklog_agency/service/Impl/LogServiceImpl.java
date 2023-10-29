package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
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
import org.apache.commons.lang3.ObjectUtils;
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

    @Override
    public Result<WorkLogRes> getWorkLog(String loginId,String dayTime) {

        WorkLogRespVo workLogRespVo = logMapper.getWorkLog(loginId,dayTime);
        List<WorkLogProblem> workLogProblemList  = logMapper.getWorkLogProblem(loginId);
//        System.out.println(workLogProblemList);

        AssertUtils.isFalse(ObjectUtils.isNotEmpty(workLogRespVo), ExceptionsEnums.WorkLog.WORK_LOG_EMPTY);

        List<WorkLogProblemRes> workLogProblemResList = new ArrayList<>();

        WorkLogRes workLogRes = new WorkLogRes();
        BeanUtils.copyProperties(workLogRespVo,workLogRes);

        workLogProblemList.forEach((a)->{
            WorkLogProblemRes workLogProblemRes = new WorkLogProblemRes();
            BeanUtils.copyProperties(a,workLogProblemRes);
            workLogProblemResList.add(workLogProblemRes);
        });

        workLogRes.setWorkLogProbList(workLogProblemResList);
        workLogRes.setLogId(Integer.valueOf(loginId));

        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId).eq(WorkLog::getUserId,StpUtil.getLoginIdAsLong());
        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);

        Long id = workLog.getId();
        workLogRes.setId(id.intValue());

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
}
