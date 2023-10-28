package com.liepin.worklog_agency.service.Impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogDetailMapper;
import com.liepin.worklog_agency.service.LogDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogDetailServiceImpl extends ServiceImpl<LogDetailMapper, WorkLogDetail> implements LogDetailService {
    @Autowired
    private LogDetailMapper logDetailMapper;
    @Override
    public Result insertWorkLogDetail(WorkLogRespVo workLogRespVo) {
        WorkLogDetail workLogDetail = new WorkLogDetail();
        BeanUtils.copyProperties(workLogRespVo,workLogDetail);
        saveOrUpdate(workLogDetail);
        return Result.success("插入or更新成功");
    }
}
