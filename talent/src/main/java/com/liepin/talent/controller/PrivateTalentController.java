package com.liepin.talent.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.talent.entity.vo.req.*;
import com.liepin.talent.entity.vo.resp.*;
import com.liepin.talent.service.PrivateTalentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "人才部私库")
@RestController
@RequestMapping("/api/talentPrivate")
public class PrivateTalentController {

    private final PrivateTalentService privateTalentService;

    @Autowired
    public PrivateTalentController(PrivateTalentService privateTalentService){
        this.privateTalentService = privateTalentService;
    }

    @PostMapping("/getNotContacted")
    @ApiOperation(value = "人才部-获取私库未联系列表")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetNotContactRespVO> getNotContact(@RequestBody GetNotContactReqVO reqVO){
        return privateTalentService.getNotContact(reqVO);
    }

    @PostMapping("/managerGetAudit")
    @ApiOperation(value = "管理员-获取扔回公海待审核/通过/不通过审核记录")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<GetAuditRespVO> managerGetAudit(@RequestBody GetAuditReqVO reqVO){
        return privateTalentService.managerGetAudit(reqVO);
    }

    @PostMapping("/audit")
    @ApiOperation(value = "管理员-审核请求")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result audit(@RequestBody AuditReqVO reqVO){
        return privateTalentService.audit(reqVO);
    }

    @PostMapping("/throwback")
    @ApiOperation(value = "人才部-扔回公海")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result throwback(@RequestBody ThrowbackReqVO reqVO){
        return privateTalentService.throwback(reqVO);
    }

    @PostMapping("/getSelfAudit")
    @ApiOperation(value = "人才部-获取本人扔回公海待审核/失败/成功记录")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetAuditRespVO> getSelfAudit(@RequestBody GetAuditReqVO reqVO){
        return privateTalentService.getSelfAudit(reqVO);
    }



    @PostMapping("/addTalent")
    @ApiOperation(value = "人才部-新增未联系人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result addTalent(@RequestBody AddTalentReqVO reqVO){
        return privateTalentService.addTalent(reqVO);
    }

    @GetMapping("/followupInfo")
    @ApiOperation(value = "人才部-跟进详情")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<FollowupInfoRespVO> followupInfo(@RequestParam @Parameter(description = "私库id") Long id){
        return privateTalentService.followupInfo(id);
    }

    @PostMapping("/followupTalent")
    @ApiOperation(value = "人才部-跟进人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result followupTalent(@RequestBody FollowupTalentReqVO reqVO){
        return privateTalentService.followupTalent(reqVO);
    }

    @PostMapping("/uploadIdPic")
    @ApiOperation(value = "人才部-上传身份证照片")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result uploadIdPic(@RequestBody UploadIdPicReqVO reqVO){
        return privateTalentService.uploadIdPic(reqVO);
    }

    @PostMapping("/getFollowup")
    @ApiOperation(value = "人才部-获取跟进中人才列表")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetFollowupRespVO> getFollowup(@RequestBody GetFollowupReqVO reqVO){
        return privateTalentService.getFollowup(reqVO);
    }

    @GetMapping("/getAllFollowup")
    @ApiOperation(value = "人才部-获取所有跟进中人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetAllFollowupRespVO> getAllFollowup(){
        return privateTalentService.getAllFollowup();
    }

    @PostMapping("/sendTo")
    @ApiOperation(value = "人才部-内推")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result sendTo(@RequestBody SendToReqVO reqVO){
        return privateTalentService.sendTo(reqVO);
    }

    @PostMapping("/getSendHistory")
    @ApiOperation(value = "人才部-获取自己内推记录")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result<GetSendHistoryRespVO> getSendHistory(@RequestBody GetSendHistoryReqVO reqVO){
        return privateTalentService.getSendHistory(reqVO);
    }

    @PostMapping("/getSendAudit")
    @ApiOperation(value = "总经办-获取内推记录")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<GetSendHistoryRespVO> getSendAudit(@RequestBody GetSendAuditReqVO reqVO){
        return privateTalentService.getSendAudit(reqVO);
    }

    @PostMapping("/auditSend")
    @ApiOperation(value = "总经办-审核内推")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result auditSend(@RequestBody AuditSendReqVO reqVO){
        return privateTalentService.auditSend(reqVO);
    }

    @GetMapping("/match")
    @ApiOperation(value = "人才部-匹配人才")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result readyMatch(@RequestParam @Parameter(description = "私库id") Long id){
        return privateTalentService.readyMatch(id);
    }


}
