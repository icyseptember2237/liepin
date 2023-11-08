package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;

public interface LogProblemService extends IService<WorkLogProblem> {
    Result insertWorkLogProblem(WorkLogRespVo workLogRespVo);

    Result insertOtherWorkLogProblem(WorkLogRespVo workLogRespVo);

}
