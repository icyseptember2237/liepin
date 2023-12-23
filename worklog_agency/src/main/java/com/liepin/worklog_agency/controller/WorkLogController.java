package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.aspect.operationAspect.OperationAspect;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.OperationModule;
import com.liepin.common.constant.enums.OperationType;
import com.liepin.worklog_agency.entity.request.GetWorkLogReqVO;
import com.liepin.worklog_agency.entity.response.*;
import com.liepin.worklog_agency.service.LogBriefService;
import com.liepin.worklog_agency.service.LogDetailService;
import com.liepin.worklog_agency.service.LogProblemService;
import com.liepin.worklog_agency.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @OperationAspect(detail = "获取个人工作日志",type = OperationType.SERACH.value ,module = OperationModule.WORKLOG.value)
        public Result<WorkLogRes> getLog(){
        long loginId = StpUtil.getLoginIdAsLong();
        return logService.getWorkLog(loginId);
    }
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.TALENT.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    @PostMapping("/postWorkLog")
    @ApiOperation(value = "更新or上传日志")
    @OperationAspect(detail = "更新or上传日志", type = OperationType.INSERT.value, module = OperationModule.WORKLOG.value)
        public Result postLog(@RequestBody WorkLogRespVo workLogRespVo){
        logService.insertWorkLog(workLogRespVo);
        logDetailService.insertWorkLogDetail(workLogRespVo);
        logProblemService.insertWorkLogProblem(workLogRespVo);
        return Result.success();
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/getAllWorkLog")
    @ApiOperation(value = "管理员获取简要日志")
    @OperationAspect(detail = "管理员获取简要日志", type = OperationType.SERACH.value, module = OperationModule.WORKLOG.value)
        public Result<GetWorkLogRespVO> getAllLog(@RequestBody GetWorkLogReqVO reqVO){
        return logBriefService.getAllWork(reqVO);
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/getSomeoneWorkLog")
    @ApiOperation(value = "管理员获取某个人日志")
    @OperationAspect(detail = "管理员获取某个日志", type = OperationType.SERACH.value, module = OperationModule.WORKLOG.value)
        public Result<WorkLogRes> getSomeoneWorkLog(@RequestParam Long logId){
            return logService.getWorkLogByLogId(logId);
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/updateOthersWorkLog")
    @ApiOperation(value = "管理员更改其他人日志")
    @OperationAspect(detail = "管理员更改其他人日志", type = OperationType.UPDATE.value, module = OperationModule.WORKLOG.value)
    public Result updateOthersWorkLog(@RequestBody WorkLogRespVo workLogRespVo){

        logService.insertOtherWorkLog(workLogRespVo);
        logDetailService.insertOtherWorkLogDetail(workLogRespVo);
        logProblemService.insertOtherWorkLogProblem(workLogRespVo);
        return Result.success();
    }
    @SaCheckLogin
    @GetMapping("/getLastWorkLog")
    @ApiOperation(value = "获取昨天的日志")
    @OperationAspect(detail = "获取昨天的日志", type = OperationType.SERACH.value, module = OperationModule.WORKLOG.value)
    public Result<WorkLogRes> getLastWorkLog(){
        return logService.getLastWorkLog(StpUtil.getLoginIdAsLong());
    }
}
