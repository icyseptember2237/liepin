package com.liepin.enterprise.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.enterprise.entity.vo.req.*;
import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.resp.GetNotContactRespVO;
import com.liepin.enterprise.entity.vo.resp.GetAuditRespVO;
import com.liepin.enterprise.service.PrivateEnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/managerGetAudit")
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
    public Result<GetAuditRespVO> getSelfAudit(@RequestBody GetAuditReqVO reqVO){
        return enterprisePrivateService.getSelfAudit(reqVO);
    }




    @PostMapping("/addEnterprise")
    @ApiOperation(value = "单位部-新增未联系单位")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result addEnterprise(@RequestBody AddEnterpriseReqVO reqVO){
        return enterprisePrivateService.addEnterprise(reqVO);
    }

}
