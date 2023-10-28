package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.mapper.LogDetailMapper;

public interface LogDetailService extends IService<WorkLogDetail> {
    Result insertWorkLogDetail(WorkLogRespVo workLogRespVo);
}
