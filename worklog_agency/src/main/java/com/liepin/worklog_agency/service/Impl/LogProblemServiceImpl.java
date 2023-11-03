package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.request.InsertProblemLogReqVO;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.mapper.LogProblemMapper;
import com.liepin.worklog_agency.service.LogProblemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogProblemServiceImpl extends ServiceImpl<LogProblemMapper,WorkLogProblem> implements LogProblemService {
    @Autowired
    private LogProblemMapper logProblemMapper;
    @Autowired
    private LogMapper logMapper;
    @Override
    public Result insertWorkLogProblem(WorkLogRespVo workLogRespVo) {
        // 得到日志id
        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId).eq(WorkLog::getUserId,StpUtil.getLoginIdAsLong()).like(WorkLog::getCreateTime, TimeUtil.getToday());
        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
        Long id = workLog.getId();
        // 全覆盖更新
        //删除
        LambdaQueryWrapper<WorkLogProblem> lambdaQueryWrapperForProblem = new LambdaQueryWrapper<>();
        lambdaQueryWrapperForProblem.eq(WorkLogProblem::getId,id);
        WorkLogProblem workLogProblemDlt = new WorkLogProblem();
        workLogProblemDlt.setDlt(ConstantsEnums.YESNO.YES.getValue());
        logProblemMapper.update(workLogProblemDlt,lambdaQueryWrapperForProblem);

        //更新
        List<InsertProblemLogReqVO> workLogProblemList = workLogRespVo.getInsertProblemLogReqVOList();

        List<WorkLogProblem> logProblemList = new ArrayList<>();
        for(InsertProblemLogReqVO insertProblemLogReqVO:workLogProblemList){
            WorkLogProblem workLogProblem = new WorkLogProblem();
            BeanUtils.copyProperties(insertProblemLogReqVO,workLogProblem);
            workLogProblem.setId(id);
            logProblemList.add(workLogProblem);
        }

        saveBatch(logProblemList);
//        saveOrUpdateBatch(logProblemList);
        return Result.success("插入问题成功");
    }
}
