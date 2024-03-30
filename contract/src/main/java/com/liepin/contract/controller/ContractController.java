package com.liepin.contract.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.aspect.ratelimit.RateLimit;
import com.liepin.common.constant.classes.Result;
import com.liepin.contract.aspect.LockContract;
import com.liepin.contract.entity.vo.list.ContractRequireListVO;
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
    @SaCheckRole(value = {RoleType.ENTERPRISE.code})
    @LockContract
    @RateLimit(times = 60,byId = false)
    public Result SendAudit(@RequestParam @Parameter(description = "合同id") Long contractId){
        return contractService.SendAudit(contractId);
    }

    @PostMapping("/getAudit")
    @ApiOperation(value = "总经办、单位部-根据审核状态获取合同")
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    public Result<GetContractAuditRespVO> getAudit(@RequestBody GetContractAuditReqVO reqVO){
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
    @ApiOperation(value = "所有-获取匹配中需求")
    @SaCheckLogin
    public Result<GetContractsRespVO> getContracts(@RequestBody GetContractsReqVO reqVO){
        return contractService.getContracts(reqVO);
    }

    @GetMapping("/getContractInfo")
    @ApiOperation(value = "所有人-根据合同id获取合同详情")
    @SaCheckLogin
    public Result<GetContractInfoRespVO> getContractInfo(@RequestParam Long contractId){
        return contractService.getContractInfo(contractId);
    }

    @GetMapping("/getRequireInfo")
    @ApiOperation(value = "所有人-根据需求id获取需求详情")
    @SaCheckLogin
    Result<ContractRequireListVO> getRequireInfo(@RequestParam Long requireId){
        return contractService.getRequireInfo(requireId);
    }

    @PostMapping("/getSelfContract")
    @ApiOperation(value = "单位部-查看自己的合同")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result<GetSelfContractRespVO> getSelfContract(@RequestBody GetSelfContractReqVO reqVO){
        return contractService.getSelfContract(reqVO);
    }

    @PostMapping("/uploadContract")
    @ApiOperation(value = "单位部-上传合同复印件")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    @LockContract
    public Result uploadContract(@RequestBody UploadContractReqVO reqVO){
        return contractService.uploadContract(reqVO);
    }

    @PostMapping("/getSelfMatches")
    @ApiOperation(value = "人才部-按状态获取自己匹配的人才信息")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetSelfMatchesRespVO> getSelfMatches(@RequestBody GetSelfMatchesReqVO reqVO){
        return contractService.getSelfMatches(reqVO);
    }

    @PostMapping("/match")
    @ApiOperation(value = "人才部-匹配人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    @LockContract
    public Result match(@RequestBody MatchReqVO reqVO){
        return contractService.match(reqVO);
    }

    @PostMapping("/cancelMatch")
    @ApiOperation(value = "人才部-取消人才匹配")
    @SaCheckRole(value = RoleType.TALENT.code)
    @LockContract
    public Result cancelMatch(@RequestBody CancelMatchReqVO reqVO){
        return contractService.cancelMatch(reqVO);
    }

    @PostMapping("/registerMoney")
    @ApiOperation(value = "单位部-认款")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result registerMoney(@RequestBody RegisterMoneyReqVO reqVO){
        return contractService.registerMoney(reqVO);
    }

    @PostMapping("/auditMoney")
    @ApiOperation(value = "总经办-审核认款")
    @SaCheckRole(value = RoleType.MANAGER.code)
    @LockContract
    public Result auditMoney(@RequestBody AuditMoneyReqVO reqVO){
        return contractService.auditMoney(reqVO);
    }

    @PostMapping("/sharePerformance")
    @ApiOperation(value = "单位部，总经办-分业绩")
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    @LockContract
    public Result sharePerformance(@RequestParam Long contractId){
        return contractService.sharePerformance(contractId);
    }

    @PostMapping("/getRegisterMoneyAudit")
    @ApiOperation(value = "总经办、单位部-根据审核状态获取单位部认款信息(单位部调用只获取本人的)")
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    public Result<GetRegisterMoneyAuditRespVO> getRegisterMoneyAudit(@RequestBody GetRegisterMoneyAuditReqVO reqVO){
        return contractService.getRegisterMoneyAudit(reqVO);
    }

    @PostMapping("/enterpriseApplyMoney")
    @ApiOperation(value = "单位部-从合同申请资金")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result enterpriseApplyMoney(@RequestBody ApplyMoneyReqVO reqVO){
        return contractService.enterpriseApplyMoney(reqVO);
    }
    @PostMapping("/talentApplyMoney")
    @ApiOperation(value = "人才部-从合同申请资金给人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result talentApplyMoney(@RequestBody TalentApplyMoneyReqVO reqVO){
        return contractService.talentApplyMoney(reqVO);
    }

    @PostMapping("/getEnterpriseApplyMoneyAudit")
    @ApiOperation(value = "总经办、单位部-根据审核状态获取单位部申请资金")
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.ENTERPRISE.code},mode = SaMode.OR)
    public Result<GetEnterpriseApplyMoneyAuditRespVO> getEnterpriseApplyMoneyAudit(@RequestBody GetApplyMoneyAuditReqVO reqVO){
        return contractService.getEnterpriseApplyMoneyAudit(reqVO);
    }

    @PostMapping("/getTalentApplyMoneyAudit")
    @ApiOperation(value = "总经办、人才部-根据审核状态获取人才部申请资金")
    @SaCheckRole(value = {RoleType.MANAGER.code,RoleType.TALENT.code},mode = SaMode.OR)
    public Result<GetTalentApplyMoneyAuditRespVO> getTalentApplyMoneyAudit(@RequestBody GetApplyMoneyAuditReqVO reqVO){
        return contractService.getTalentApplyMoneyAudit(reqVO);
    }

    @PostMapping("/auditEnterpriseApply")
    @ApiOperation(value = "总经办-审核单位部申请资金")
    @SaCheckRole(value = RoleType.MANAGER.code)
    @LockContract
    public Result auditEnterpriseApply(@RequestBody AuditApplyReqVO reqVO){
        return contractService.auditEnterpriseApply(reqVO);
    }

    @PostMapping("/auditTalentApply")
    @ApiOperation(value = "总经办-审核人才部申请资金")
    @SaCheckRole(value = RoleType.MANAGER.code)
    @LockContract
    public Result auditTalentApply(@RequestBody AuditApplyReqVO reqVO){
        return contractService.auditTalentApply(reqVO);
    }

    @PostMapping("/reDoEnterprise")
    @ApiOperation(value = "单位部-将完结状态的合同对应的单位重新移至跟进中")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    @LockContract
    public Result reDoEnterprise(@RequestBody RedoReqVO reqVO){
        return contractService.reDoEnterprise(reqVO.getContractId());
    }

    @PostMapping("/reDoTalent")
    @ApiOperation(value = "人才部-将完结状态的合同对应的人才重新移至跟进中")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result reDoTalent(@RequestBody RedoReqVO reqVO){
        return contractService.reDoTalent(reqVO.getContractId());
    }
}
