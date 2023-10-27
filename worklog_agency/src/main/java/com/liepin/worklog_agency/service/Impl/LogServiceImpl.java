package com.liepin.worklog_agency.service.Impl;

import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Override
    public Result getWorkLog(String loginId) {
        String WorkLogId = logMapper.getWorkLog(loginId);
        return Result.success();
    }
}
