package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worklog")
public class WorkLogController {
//    获取日志
    @Autowired
    private LogService logService;



    @GetMapping("/getWorkLog")
        public Result getLog(){
        String loginId = StpUtil.getLoginId().toString();
        return logService.getWorkLog(loginId);
    }
}
