package com.liepin.enterprise.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.liepin.auth.constant.RoleType;
import com.liepin.enterprise.entity.vo.req.*;
import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.resp.*;
import com.liepin.enterprise.service.PrivateEnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "单位部私库")
@RestController
@RequestMapping("/api/enterprisePrivate")
public class PrivateEnterpriseController {

    private final PrivateEnterpriseService enterprisePrivateService;

    @Autowired
    public PrivateEnterpriseController(PrivateEnterpriseService enterprisePrivateService){
        this.enterprisePrivateService = enterprisePrivateService;
    }

    @PostMapping("/getNotContacted")
    @ApiOperation(value = "单位部-获取私库未联系列表")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetNotContactRespVO> getNotContact(@RequestBody GetNotContactReqVO reqVO){
        return enterprisePrivateService.getNotContact(reqVO);
    }

    @PostMapping("/managerGetAudit")
    @ApiOperation(value = "管理员-获取扔回公海待审核/通过/不通过审核记录")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<GetAuditRespVO> managerGetAudit(@RequestBody GetAuditReqVO reqVO){
        return enterprisePrivateService.managerGetAudit(reqVO);
    }

    @PostMapping("/audit")
    @ApiOperation(value = "管理员-审核请求")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result audit(@RequestBody AuditReqVO reqVO){
        return enterprisePrivateService.audit(reqVO);
    }

    @PostMapping("/throwback")
    @ApiOperation(value = "单位部-扔回公海")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result throwback(@RequestBody ThrowbackReqVO reqVO){
        return enterprisePrivateService.throwback(reqVO);
    }

    @PostMapping("/getSelfAudit")
    @ApiOperation(value = "单位部-获取本人扔回公海待审核/失败/成功记录")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetAuditRespVO> getSelfAudit(@RequestBody GetAuditReqVO reqVO){
        return enterprisePrivateService.getSelfAudit(reqVO);
    }




    @PostMapping("/addEnterprise")
    @ApiOperation(value = "单位部-新增未联系单位")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result addEnterprise(@RequestBody AddEnterpriseReqVO reqVO){
        return enterprisePrivateService.addEnterprise(reqVO);
    }

    @GetMapping("/followupInfo")
    @ApiOperation(value = "单位部-跟进详情")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<FollowupInfoRespVO> followupInfo(@RequestParam @Parameter(description = "私库id") Long id){
        return enterprisePrivateService.followupInfo(id);
    }

    @PostMapping("/followupEnterprise")
    @ApiOperation(value = "单位部-跟进单位")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result followupEnterprise(@RequestBody FollowupEnterpriseReqVO reqVO){
        return enterprisePrivateService.followupEnterprise(reqVO);
    }

    @PostMapping("/getFollowup")
    @ApiOperation(value = "单位部-获取跟进中单位列表")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetFollowupRespVO> getFollowup(@RequestBody GetFollowupReqVO reqVO){
        return enterprisePrivateService.getFollowup(reqVO);
    }

    @PostMapping("/sendTo")
    @ApiOperation(value = "单位部-内推")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result sendTo(@RequestBody SendToReqVO reqVO){
        return enterprisePrivateService.sendTo(reqVO);
    }

    @PostMapping("/getSendHistory")
    @ApiOperation(value = "单位部-获取自己内推记录")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetSendHistoryRespVO> getSendHistory(@RequestBody GetSendHistoryReqVO reqVO){
        return enterprisePrivateService.getSendHistory(reqVO);
    }

    @PostMapping("/getSendAudit")
    @ApiOperation(value = "总经办-获取内推记录")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<GetSendHistoryRespVO> getSendAudit(@RequestBody GetSendAuditReqVO reqVO){
        return enterprisePrivateService.getSendAudit(reqVO);
    }

    @PostMapping("/auditSend")
    @ApiOperation(value = "总经办-审核内推")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result auditSend(@RequestBody AuditSendReqVO reqVO){
        return enterprisePrivateService.auditSend(reqVO);
    }

}
