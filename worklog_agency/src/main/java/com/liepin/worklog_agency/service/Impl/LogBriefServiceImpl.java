package com.liepin.worklog_agency.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.response.WorkLogBriefRes;
import com.liepin.worklog_agency.mapper.LogBriefMapper;
import com.liepin.worklog_agency.service.LogBriefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogBriefServiceImpl extends ServiceImpl<LogBriefMapper,WorkLogBriefRes> implements LogBriefService{
    @Autowired
    private LogBriefMapper logBriefMapper;
    @Override
    public Result<List<WorkLogBriefRes>> getAllWork(String dayTime) {
//        LambdaQueryWrapper<WorkLogBriefRes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(WorkLogBriefRes::getUpdateTime,dayTime);
//        List<WorkLogBriefRes> workLogBriefResList = logBriefMapper.selectList(lambdaQueryWrapper);
        List<WorkLogBriefRes> workLogBriefResList = logBriefMapper.getAllBriefLog(dayTime);
        return Result.success(workLogBriefResList);
    }
}
