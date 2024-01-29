package com.liepin.contract.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.config.exception.BusinessException;
import com.liepin.common.constant.classes.Result;
import com.liepin.contract.aspect.LockContract;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;
import com.liepin.contract.service.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "合同操作")
@RestController
@RequestMapping("/api/contract")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService){
        this.contractService = contractService;
    }

    @GetMapping("/getContractsByPrivateId")
    @ApiOperation(value = "单位部-根据单位id获取单位下所有合同")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetContractsByPrivateIdRespVO> getContractsByPrivateId(@RequestParam @Parameter(description = "单位私库id") Long id){
        return contractService.getContractsByPrivateId(id);
    }

    @PostMapping("/newContract")
    @ApiOperation(value = "单位部-新建合同需求")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result newContract(@RequestBody NewContractReqVO reqVO){
        return contractService.newContract(reqVO);
    }

    @GetMapping("/deleteContract")
    @ApiOperation(value = "总经办、单位部-删除还在匹配中的合同")
    @SaCheckRole(value = {RoleType.ENTERPRISE.code,RoleType.MANAGER.code},mode = SaMode.OR)
    @LockContract
    public Result deleteContract(@RequestParam @Parameter(description = "合同id") Long contractId){
        return contractService.deleteContract(contractId);
    }

    @GetMapping("/sendAudit")
    @ApiOperation(value = "单位部-合同提交审批")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    @LockContract
    public Result SendAudit(@RequestParam @Parameter(description = "合同id") Long contractId){
        return contractService.SendAudit(contractId);
    }

    @PostMapping("/getAudit")
    @ApiOperation(value = "总经办、单位部-获取合同审核记录")
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    public Result<GetAuditRespVO> getAudit(@RequestBody GetAuditReqVO reqVO){
        return contractService.getAudit(reqVO);
    }

    @PostMapping("/auditContrat")
    @ApiOperation(value = "总经办-审批合同")
    @SaCheckRole(value = RoleType.MANAGER.code)
    @LockContract
    public Result auditContract(@RequestBody AuditContractReqVO reqVO){
        return contractService.auditContract(reqVO);
    }

    @PostMapping("/getContracts")
    @ApiOperation(value = "人才部-获取匹配中合同")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetContractsRespVO> getContracts(@RequestBody GetContractsReqVO reqVO){
        return contractService.getContracts(reqVO);
    }

    @GetMapping("/getContractInfo")
    @ApiOperation(value = "所有人-根据合同id获取合同详情")
    @SaCheckLogin
    public Result<GetContractInfoRespVO> getContractInfo(@RequestParam Long contractId){
        return contractService.getContractInfo(contractId);
    }

    @GetMapping("/getSelfContract")
    @ApiOperation(value = "单位部-查看自己的合同")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetSelfContractRespVO> getSelfContract(){
        return contractService.getSelfContract();
    }

    @PostMapping("/uploadContract")
    @ApiOperation(value = "单位部-上传合同复印件")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    @LockContract
    public Result uploadContract(@RequestBody UploadContractReqVO reqVO){
        return contractService.uploadContract(reqVO);
    }

    @PostMapping("/match")
    @ApiOperation(value = "人才部-匹配人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    @LockContract
    public Result match(@RequestBody MatchReqVO reqVO){
        return contractService.match(reqVO);
    }

    @PostMapping("/cancelMatch")
    @ApiOperation(value = "单位部-取消人才匹配")
    @SaCheckRole(value = RoleType.TALENT.code)
    @LockContract
    public Result cancelMatch(@RequestBody CancelMatchReqVO reqVO){
        return contractService.cancelMatch(reqVO);
    }
}
