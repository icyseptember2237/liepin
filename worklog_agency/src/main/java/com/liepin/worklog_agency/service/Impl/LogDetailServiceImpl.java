package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogDetailMapper;
import com.liepin.worklog_agency.mapper.LogMapper;
import com.liepin.worklog_agency.mapper.LogProblemMapper;
import com.liepin.worklog_agency.service.LogDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

@Service
public class LogDetailServiceImpl extends ServiceImpl<LogDetailMapper, WorkLogDetail> implements LogDetailService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Result insertWorkLogDetail(WorkLogRespVo workLogRespVo) {
        WorkLogDetail workLogDetail = new WorkLogDetail();
        BeanUtils.copyProperties(workLogRespVo,workLogDetail);
        Long userId;
        if (null!=workLogRespVo.getId()){
            userId = StpUtil.getLoginIdAsLong();
//            userId = logMapper.selectOne(new LambdaQueryWrapper<WorkLog>().eq(WorkLog::getId, workLogRespVo.getId())).getUserId();
        }else{
            userId = StpUtil.getLoginIdAsLong();
        }
        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId).eq(WorkLog::getUserId,userId).
                like(WorkLog::getCreateTime, TimeUtil.getToday()).last("limit 1");
        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
        Long id = workLog.getId();

        workLogDetail.setId(id);
        saveOrUpdate(workLogDetail);
        return Result.success("插入or更新成功");
    }

    @Override
    public Result insertOtherWorkLogDetail(WorkLogRespVo workLogRespVo) {
        WorkLogDetail workLogDetail = new WorkLogDetail();
        BeanUtils.copyProperties(workLogRespVo,workLogDetail);

//        Long logId = workLogRespVo.getId();
//        Long userId = logMapper.selectOne(new LambdaQueryWrapper<WorkLog>().eq(WorkLog::getId, workLogRespVo.getId())).getUserId();

//        workLogDetail

//        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.select(WorkLog::getId).eq(WorkLog::getUserId,userId).
//                like(WorkLog::getCreateTime, TimeUtil.getToday());
//        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
//        Long id = workLog.getId();
//
//        workLogDetail.setId(id);

        saveOrUpdate(workLogDetail);
        return Result.success("插入or更新成功");
    }

    @Override
    public Result insertLastWorkLogDetail(WorkLogRespVo workLogRespVo) {
        WorkLogDetail workLogDetail = new WorkLogDetail();
        BeanUtils.copyProperties(workLogRespVo,workLogDetail);
        Long userId = StpUtil.getLoginIdAsLong();
//        if (null!=workLogRespVo.getId()){
//            userId = StpUtil.getLoginIdAsLong();
//        }else{
//            userId = StpUtil.getLoginIdAsLong();
//        }

//        String lastWorkDay = TimeUtil.getLastWorkDayAsSec();
        Object lastWorkDay = redisTemplate.opsForValue().get("lastWorkDay");

        LambdaQueryWrapper<WorkLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(WorkLog::getId)
                .eq(WorkLog::getUserId,userId)
                .eq(WorkLog::getDlt,"NO")
                .like(WorkLog::getCreateTime, lastWorkDay)
                .last("limit 1");
        WorkLog workLog = logMapper.selectOne(lambdaQueryWrapper);
        Long id = workLog.getId();

        workLogDetail.setId(id);
        saveOrUpdate(workLogDetail);
        return Result.success("插入or更新成功");
    }
}
