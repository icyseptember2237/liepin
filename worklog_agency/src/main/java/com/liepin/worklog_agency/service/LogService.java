package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;

public interface LogService extends IService<WorkLog> {

    Result<WorkLogRes> getWorkLog(Long loginId);

    Result insertWorkLog(WorkLogRespVo workLogRespVo);

    Result<WorkLogRes> getWorkLogByLogId(Long logId);

    Result insertOtherWorkLog(WorkLogRespVo workLogRespVo);

}
