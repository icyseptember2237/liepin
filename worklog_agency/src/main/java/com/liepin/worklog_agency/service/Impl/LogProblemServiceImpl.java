package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogProblemMapper;
import com.liepin.worklog_agency.service.LogProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogProblemServiceImpl extends ServiceImpl<LogProblemMapper,WorkLogProblem> implements LogProblemService {
    @Autowired
    private LogProblemMapper logProblemMapper;
    @Override
    public Result insertWorkLogProblem(WorkLogRespVo workLogRespVo) {

        List<WorkLogProblem> workLogProblemList = workLogRespVo.getWorkLogProbList();
        for(WorkLogProblem workLogProblem:workLogProblemList){
            long id = StpUtil.getLoginIdAsLong();
            workLogProblem.setId(id);
        }
        saveOrUpdateBatch(workLogProblemList);
        return Result.success("插入问题成功");
    }
}
