package com.liepin.worklog_agency.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.response.WorkLogBriefRes;
import com.liepin.worklog_agency.mapper.LogBriefMapper;
import com.liepin.worklog_agency.service.LogBriefService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogBriefServiceImpl extends ServiceImpl<LogBriefMapper,WorkLogBriefRes> implements LogBriefService{

    @Override
    public Result<List<WorkLogBriefRes>> getAllWork() {
        return null;
    }
}
