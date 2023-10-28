package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.liepin.common.constant.classes.Result;
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

    @GetMapping("/getWorkLog")
        public Result getLog(){
        StpUtil.login(101);
        String loginId = StpUtil.getLoginId().toString();
        return logService.getWorkLog(loginId);
    }
    @PostMapping("/postWorkLog")
        public Result postLog(@RequestBody WorkLogRespVo workLogRespVo){
        StpUtil.login(102);
        workLogRespVo.setLogId(Long.valueOf(StpUtil.getLoginId().toString()));
        logProblemService.insertWorkLogProblem(workLogRespVo);
        logDetailService.insertWorkLogDetail(workLogRespVo);
        return logService.insertWorkLog(workLogRespVo);
    }

}
