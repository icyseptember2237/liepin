package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.response.WorkLogBriefRes;

import java.util.List;

public interface LogBriefService extends IService<WorkLogBriefRes> {

    Result<List<WorkLogBriefRes>> getAllWork();
}
