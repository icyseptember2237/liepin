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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class LogProblemServiceImpl extends ServiceImpl<LogProblemMapper,WorkLogProblem> implements LogProblemService {
    @Autowired
    private LogProblemMapper logProblemMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Result insertWorkLogProblem(WorkLogRespVo workLogRespVo) {
        // 得到日志id
        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId)
                .eq(WorkLog::getUserId,StpUtil.getLoginIdAsLong())
                .like(WorkLog::getCreateTime, TimeUtil.getToday())
                .last("limit 1");
        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
        Long id = workLog.getId();
        // 全覆盖更新
        //删除
        LambdaQueryWrapper<WorkLogProblem> lambdaQueryWrapperForProblem = new LambdaQueryWrapper<>();
        lambdaQueryWrapperForProblem.eq(WorkLogProblem::getId,id);
        WorkLogProblem workLogProblemDlt = new WorkLogProblem();
        workLogProblemDlt.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
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

        saveOrUpdateBatch(logProblemList);
//        saveOrUpdateBatch(logProblemList);
        return Result.success("插入问题成功");
    }

    @Override
    public Result insertOtherWorkLogProblem(WorkLogRespVo workLogRespVo) {
        Long id = workLogRespVo.getId();
        // 得到日志id
//        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.select(WorkLog::getId).eq(WorkLog::getUserId,StpUtil.getLoginIdAsLong()).like(WorkLog::getCreateTime, TimeUtil.getToday());
//        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
//        Long id = workLog.getId();
        // 全覆盖更新
        //删除
        LambdaQueryWrapper<WorkLogProblem> lambdaQueryWrapperForProblem = new LambdaQueryWrapper<>();
        lambdaQueryWrapperForProblem.eq(WorkLogProblem::getId,id);
        WorkLogProblem workLogProblemDlt = new WorkLogProblem();
        workLogProblemDlt.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
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

        saveOrUpdateBatch(logProblemList);
//        saveOrUpdateBatch(logProblemList);
        return Result.success("插入问题成功");
    }

    @Override
    public Result insertLastWorkLogProblem(WorkLogRespVo workLogRespVo) {

        Object lastWorkDay = redisTemplate.opsForValue().get("lastWorkDay");

        // 得到日志id
        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId)
                .eq(WorkLog::getUserId,StpUtil.getLoginIdAsLong())
                .eq(WorkLog::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue())
                .like(WorkLog::getCreateTime, lastWorkDay)
                .orderByDesc(WorkLog::getCreateTime)
                .last("limit 1");



        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
        Long id = workLog.getId();
        // 全覆盖更新
        //删除
        LambdaQueryWrapper<WorkLogProblem> lambdaQueryWrapperForProblem = new LambdaQueryWrapper<>();
        lambdaQueryWrapperForProblem.eq(WorkLogProblem::getId,id);
        WorkLogProblem workLogProblemDlt = new WorkLogProblem();
        workLogProblemDlt.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
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

        saveOrUpdateBatch(logProblemList);
//        saveOrUpdateBatch(logProblemList);
        return Result.success("插入问题成功");
    }
}
