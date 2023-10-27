package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public Result getWorkLog(String loginId) {
        WorkLogRespVo workLogRespVo = logMapper.getWorkLog(loginId);
        List<WorkLogProblem> workLogProblemList  = logMapper.getWorkLogProblem(loginId);
        workLogRespVo.setWorkLogProbList(workLogProblemList);
        return Result.success(workLogRespVo);
    }

    @Override
    public Result insertWorkLog(WorkLogRespVo workLogRespVo) {
        WorkLog workLog = new WorkLog();
        workLog.setId(workLogRespVo.getId());
        workLog.setUserId(StpUtil.getLoginIdAsLong());
        workLog.setCreateTime(String.valueOf(LocalDateTime.now()));
        workLog.setUpdateTime(String.valueOf(LocalDateTime.now()));

        WorkLogDetail workLogDetail = new WorkLogDetail();
        WorkLogProblem workLogProblem;
        logMapper.insertWorkLog(workLog);

        BeanUtils.copyProperties(workLogRespVo,workLogDetail);
        logMapper.insertWorkLogDetail(workLogDetail);


        logMapper.insertWorkLogProblem();

        return Result.success();
    }
}
