package com.liepin.worklog_agency.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;

public interface LogService {

    Result getWorkLog(String loginId);

    Result insertWorkLog(WorkLogRespVo workLogRespVo);
}
