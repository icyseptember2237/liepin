package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.response.WorkLogProblemRes;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;

public interface LogService extends IService<WorkLog> {

    Result<WorkLogRes> getWorkLog(String loginId,String dayTime);

    Result insertWorkLog(WorkLogRespVo workLogRespVo);
}
