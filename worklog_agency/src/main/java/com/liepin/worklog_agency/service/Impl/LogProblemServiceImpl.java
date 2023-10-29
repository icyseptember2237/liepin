package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.mapper.LogProblemMapper;
import com.liepin.worklog_agency.service.LogProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogProblemServiceImpl extends ServiceImpl<LogProblemMapper,WorkLogProblem> implements LogProblemService {
    @Autowired
    private LogProblemMapper logProblemMapper;
    @Autowired
    private LogMapper logMapper;
    @Override
    public Result insertWorkLogProblem(WorkLogRespVo workLogRespVo) {



        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId).eq(WorkLog::getUserId,StpUtil.getLoginIdAsLong());
        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
        Long id = workLog.getId();

        List<WorkLogProblem> workLogProblemList = workLogRespVo.getWorkLogProbList();
        LambdaQueryWrapper<WorkLogProblem> lambdaQueryWrapperForProblem = new LambdaQueryWrapper<>();
        lambdaQueryWrapperForProblem.eq(WorkLogProblem::getId,id);
        WorkLogProblem workLogProblemDlt = new WorkLogProblem();
        workLogProblemDlt.setDlt("YES");
        logProblemMapper.update(workLogProblemDlt,lambdaQueryWrapperForProblem);

        for(WorkLogProblem workLogProblem:workLogProblemList){
            workLogProblem.setId(id);
        }
        saveOrUpdateBatch(workLogProblemList);
        return Result.success("插入问题成功");
    }
}
