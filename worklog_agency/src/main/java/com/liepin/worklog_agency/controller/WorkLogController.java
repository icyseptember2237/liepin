package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.response.WorkLogProblemRes;
import com.liepin.worklog_agency.entity.response.WorkLogRes;
import com.liepin.worklog_agency.entity.response.WorkLogRespVo;
import com.liepin.worklog_agency.service.LogDetailService;
import com.liepin.worklog_agency.service.LogProblemService;
import com.liepin.worklog_agency.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worklog")
public class WorkLogController {
//    获取日志
    @Autowired
    private LogService logService;
    @Autowired
    private LogProblemService logProblemService;
    @Autowired
    private LogDetailService logDetailService;
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/getWorkLog")
        public Result<WorkLogRes> getLog(){
        String loginId = StpUtil.getLoginId().toString();
        return logService.getWorkLog(loginId);
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/postWorkLog")
        public Result postLog(@RequestBody WorkLogRespVo workLogRespVo){
        long loginIdAsLong = StpUtil.getLoginIdAsLong();

        workLogRespVo.setId(Long.valueOf(StpUtil.getLoginId().toString()));
        logService.insertWorkLog(workLogRespVo);
        logDetailService.insertWorkLogDetail(workLogRespVo);
        logProblemService.insertWorkLogProblem(workLogRespVo);
        return Result.success();
    }

}
