package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Rot;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.WorkLog;
import com.liepin.worklog_agency.entity.request.GetWorkLogReqVO;
import com.liepin.worklog_agency.entity.response.*;
import com.liepin.worklog_agency.service.LogBriefService;
import com.liepin.worklog_agency.service.LogDetailService;
import com.liepin.worklog_agency.service.LogProblemService;
import com.liepin.worklog_agency.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worklog")
@Api(tags = "日志")
public class WorkLogController {
//    获取日志
    @Autowired
    private LogService logService;
    @Autowired
    private LogProblemService logProblemService;
    @Autowired
    private LogDetailService logDetailService;
    @Autowired
    private LogBriefService logBriefService;
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.TALENT.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    @GetMapping("/getWorkLog")
    @ApiOperation(value = "获取日志")
        public Result<WorkLogRes> getLog(){
        String loginId = StpUtil.getLoginId().toString();
        return logService.getWorkLog(loginId);
    }
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.TALENT.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    @PostMapping("/postWorkLog")
    @ApiOperation(value = "更新or上传日志")
        public Result postLog(@RequestBody WorkLogRespVo workLogRespVo){
//        workLogRespVo.setId(Long.valueOf(StpUtil.getLoginId().toString()));
        logService.insertWorkLog(workLogRespVo);
        logDetailService.insertWorkLogDetail(workLogRespVo);
        logProblemService.insertWorkLogProblem(workLogRespVo);
        return Result.success();
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/getAllWorkLog")
    @ApiOperation(value = "管理员获取简要日志")
        public Result<GetWorkLogRespVO> getAllLog(@RequestBody GetWorkLogReqVO reqVO){
        return logBriefService.getAllWork(reqVO);
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/getSomeoneWorkLog")
    @ApiOperation(value = "管理员获取某个人日志")
        public Result<WorkLogRes> getSomeoneWorkLog(@RequestParam String logId){
            return logService.getWorkLogByLogId(logId);
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/updateOthersWorkLog")
    @ApiOperation(value = "管理员更改其他人日志")
    public Result updateOthersWorkLog(@RequestBody WorkLogRespVo workLogRespVo){

        logService.insertOtherWorkLog(workLogRespVo);
        logDetailService.insertOtherWorkLogDetail(workLogRespVo);
        logProblemService.insertOtherWorkLogProblem(workLogRespVo);
        return Result.success();
    }
}
