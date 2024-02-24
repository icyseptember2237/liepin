package com.liepin.talent.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.talent.constant.SendStatus;
import com.liepin.talent.constant.TalentMatchStatus;
import com.liepin.talent.constant.TalentPrivateStatus;
import com.liepin.talent.entity.base.*;
import com.liepin.talent.entity.vo.list.GetFollowupListVO;
import com.liepin.talent.entity.vo.req.*;
import com.liepin.talent.entity.vo.resp.FollowupInfoRespVO;
import com.liepin.talent.entity.vo.resp.GetFollowupRespVO;
import com.liepin.talent.entity.vo.resp.GetNotContactRespVO;
import com.liepin.talent.entity.vo.resp.GetSendHistoryRespVO;
import com.liepin.talent.mapper.PrivateTalentMapper;
import com.liepin.talent.mapper.TalentMapper;
import com.liepin.talent.mapper.base.TalentSendToMapper;
import com.liepin.talent.service.PrivateTalentService;
import com.liepin.talent.service.base.*;
import com.liepin.worklog_agency.service.Impl.AgencyServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PrivateTalentServiceImpl implements PrivateTalentService {
    private final PrivateTalentMapper privateTalentMapper;
    private final TalentThrowbackHistoryService talentThrowbackHistoryService;
    private final TalentPrivateService talentPrivateService;
    private final TalentInfoService talentInfoService;
    private final TalentPrivateFollowupService talentPrivateFollowupService;
    private final TalentSendToService sendToService;
    private final TalentSendToMapper sendToMapper;
    private final TalentMapper talentMapper;
    private final TalentMatchService talentMatchService;

    private final AgencyServiceImpl agencyService;

    @Autowired
    public PrivateTalentServiceImpl(PrivateTalentMapper privateTalentMapper, TalentThrowbackHistoryService talentThrowbackHistoryService,
                                    TalentPrivateService talentPrivateService, TalentInfoService talentInfoService,
                                    TalentPrivateFollowupService talentPrivateFollowupService, TalentSendToService sendToService,
                                    TalentSendToMapper sendToMapper, TalentMapper talentMapper,TalentMatchService talentMatchService,
                                    AgencyServiceImpl agencyService){
        this.agencyService = agencyService;
        this.talentMatchService = talentMatchService;
        this.talentMapper = talentMapper;
        this.sendToMapper = sendToMapper;
        this.sendToService = sendToService;
        this.talentPrivateFollowupService = talentPrivateFollowupService;
        this.talentInfoService = talentInfoService;
        this.talentPrivateService = talentPrivateService;
        this.talentThrowbackHistoryService = talentThrowbackHistoryService;
        this.privateTalentMapper = privateTalentMapper;
    }


    @Override
    public Result<GetNotContactRespVO> getNotContact(GetNotContactReqVO reqVO){
        GetNotContactRespVO respVO = new GetNotContactRespVO();
        respVO.setList(privateTalentMapper.selectNotContactList(StpUtil.getLoginIdAsLong(),reqVO));
        respVO.setTotal(privateTalentMapper.selectNotContactListNum(StpUtil.getLoginIdAsLong(),reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<GetAuditRespVO> managerGetAudit(@RequestBody GetAuditReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getAuditStatus()), ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        GetAuditRespVO respVO = new GetAuditRespVO();
        respVO.setList(privateTalentMapper.selectAuditList(reqVO,null));
        respVO.setTotal(privateTalentMapper.selectAuditListNum(reqVO,null));
        return Result.success(respVO);
    }

    @Override
    public Result audit(AuditReqVO reqVO){
        TalentThrowbackHistory history = talentThrowbackHistoryService.getById(reqVO.getId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(history),ExceptionsEnums.Common.NO_DATA);
        TalentPrivate talentPrivate = talentPrivateService.getById(history.getPrivateId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate),ExceptionsEnums.Common.NO_DATA);
        TalentInfo info = talentInfoService.getById(history.getInfoId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),ExceptionsEnums.Common.NO_DATA);

        if (ConstantsEnums.AuditStatus.PASS.getStatus().equals(reqVO.getStatus())){
            history.setAuditStatus(ConstantsEnums.AuditStatus.PASS.getStatus());
            history.setAuditTime(TimeUtil.getNowWithSec());
            talentThrowbackHistoryService.updateById(history);

            talentPrivate.setThrowback(ConstantsEnums.YESNOWAIT.YES.getValue());
            talentPrivateService.updateById(talentPrivate);

            info.setIsPrivate(ConstantsEnums.YESNOWAIT.NO.getValue());
            talentInfoService.updateById(info);
            return Result.success();
        } else if (ConstantsEnums.AuditStatus.FAIL.getStatus().equals(reqVO.getStatus())){
            history.setAuditStatus(ConstantsEnums.AuditStatus.FAIL.getStatus());
            history.setAuditTime(TimeUtil.getNowWithSec());
            talentThrowbackHistoryService.updateById(history);

            talentPrivate.setThrowback(ConstantsEnums.YESNOWAIT.NO.getValue());
            talentPrivateService.updateById(talentPrivate);
            return Result.success();
        }
        AssertUtils.throwException(ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        return null;

    }

    @Override
    public Result throwback(ThrowbackReqVO reqVO){
        TalentPrivate talentPrivate = talentPrivateService.getOne(new LambdaQueryWrapper<TalentPrivate>().eq(TalentPrivate::getId,reqVO.getId())
                .eq(TalentPrivate::getThrowback,ConstantsEnums.YESNOWAIT.NO));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate),ExceptionsEnums.Talent.NO_DATA);
        AssertUtils.isFalse( StpUtil.getLoginIdAsLong() == talentPrivate.getUserId(),
                ExceptionsEnums.Common.NO_PERMISSION);
        AssertUtils.isFalse(ConstantsEnums.YESNOWAIT.NO.getValue().equals(talentPrivate.getThrowback()),
                "重复操作");

        talentPrivate.setThrowback(ConstantsEnums.YESNOWAIT.WAIT.getValue());
        talentPrivateService.updateById(talentPrivate);

        TalentThrowbackHistory history = new TalentThrowbackHistory();
        history.setInfoId(talentPrivate.getInfoId());
        history.setPrivateId(talentPrivate.getId());
        history.setCreateTime(TimeUtil.getNowWithSec());
        history.setThrowbackReason(reqVO.getThrowbackReason());
        history.setRemark(reqVO.getRemark());
        talentThrowbackHistoryService.save(history);

        return Result.success();
    }

    @Override
    public Result<GetAuditRespVO> getSelfAudit(GetAuditReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getAuditStatus()),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        GetAuditRespVO respVO = new GetAuditRespVO();
        respVO.setList(privateTalentMapper.selectAuditList(reqVO,StpUtil.getLoginIdAsLong()));
        respVO.setTotal(privateTalentMapper.selectAuditListNum(reqVO,StpUtil.getLoginIdAsLong()));
        return Result.success(respVO);
    }

    @Override
    public Result addTalent(@RequestBody AddTalentReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getName()),
                "人才名称不能为空");
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getPhone()),
                "联系方式不能为空");

        try {
            TalentInfo info = new TalentInfo();
            BeanUtils.copyProperties(reqVO,info);
            info.setIsPrivate(ConstantsEnums.YESNOWAIT.YES.getValue());
            info.setCreateTime(TimeUtil.getNowWithSec());
            talentInfoService.save(info);

            TalentPrivate talentPrivate = new TalentPrivate();
            talentPrivate.setInfoId(info.getId());
            talentPrivate.setUserId(StpUtil.getLoginIdAsLong());
            talentPrivate.setCreateTime(TimeUtil.getNowWithSec());
            talentPrivate.setStatus(TalentPrivateStatus.NOT_CONTACT.getStatus());
            talentPrivateService.save(talentPrivate);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            AssertUtils.throwException(ExceptionsEnums.Talent.INSERT_FAIL);
        }

        return Result.success();
    }

    @Override
    public Result<FollowupInfoRespVO> followupInfo(Long id){
        FollowupInfoRespVO respVO = new FollowupInfoRespVO();
        TalentPrivate talentPrivate = talentPrivateService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate) && talentPrivate.getThrowback().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),ExceptionsEnums.Talent.NO_DATA);
        TalentInfo info = talentInfoService.getById(talentPrivateService.getById(id).getInfoId());

        BeanUtils.copyProperties(info,respVO);
        if (info.getAgencyId() != 0){
            respVO.setAgencyName(agencyService.getById(info.getAgencyId()).getEnterpriseName());
        }
        respVO.setList(talentMapper.getFollowupHistory(info.getId(),1,5));
        return Result.success(respVO);
    }

    @Override
    public Result followupTalent(FollowupTalentReqVO reqVO){
        TalentPrivate talentPrivate = talentPrivateService.getOne(new LambdaQueryWrapper<TalentPrivate>().eq(TalentPrivate::getId,reqVO.getId())
                .eq(TalentPrivate::getThrowback,ConstantsEnums.YESNOWAIT.NO));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate),ExceptionsEnums.Talent.NO_DATA);
        AssertUtils.isFalse(talentPrivate.getUserId() == StpUtil.getLoginIdAsLong(),ExceptionsEnums.Common.NO_PERMISSION);

        if (talentPrivate.getStatus().equals(TalentPrivateStatus.NOT_CONTACT.getStatus())){
            talentPrivate.setStatus(TalentPrivateStatus.FOLLOWUP.getStatus());
            talentPrivateService.updateById(talentPrivate);
        }

        TalentInfo talentInfo = new TalentInfo();
        BeanUtils.copyProperties(reqVO, talentInfo,"id");
        talentInfo.setId(talentPrivate.getInfoId());
        talentInfoService.updateById(talentInfo);

        TalentPrivateFollowup followup = new TalentPrivateFollowup();
        followup.setPrivateId(talentPrivate.getId());
        followup.setFollowupId(StpUtil.getLoginIdAsLong());
        followup.setTime(TimeUtil.getNowWithSec());
        followup.setRemark(reqVO.getFollowupRemark());
        followup.setNextTime(reqVO.getNextTime());
        talentPrivateFollowupService.save(followup);

        return Result.success();
    }

    @Override
    public Result<GetFollowupRespVO> getFollowup(GetFollowupReqVO reqVO){
        if (ObjectUtils.isNotEmpty(reqVO.getPageSize()))
            reqVO.setPageSize(Math.min(15,reqVO.getPageSize()));
        GetFollowupRespVO respVO = new GetFollowupRespVO();
        List<GetFollowupListVO> list = privateTalentMapper.selectFollowupList(reqVO,StpUtil.getLoginIdAsLong());
        respVO.setList(list);
        respVO.setTotal(privateTalentMapper.selectFollowupListNum(reqVO,StpUtil.getLoginIdAsLong()));
        return Result.success(respVO);
    }

    @Override
    public Result<GetFollowupRespVO> getAllFollowup(){
        GetFollowupRespVO respVO = new GetFollowupRespVO();
        List<GetFollowupListVO> list = privateTalentMapper.selectAllFollowupList(StpUtil.getLoginIdAsLong());
        respVO.setList(list);
        return Result.success(respVO);
    }

    @Override
    public Result sendTo(SendToReqVO reqVO){
        AssertUtils.isFalse(StpUtil.getLoginIdAsLong() != reqVO.getUserId(),"不能内推给本人");
        AssertUtils.isFalse(sendToMapper.checkDept(RoleType.TALENT.code, reqVO.getUserId()) == 1,"只能内推至部门内部");
        TalentPrivate talentPrivate = talentPrivateService.getOne(new LambdaQueryWrapper<TalentPrivate>()
                .eq(TalentPrivate::getId,reqVO.getPrivateId())
                .eq(TalentPrivate::getThrowback,ConstantsEnums.YESNOWAIT.NO));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate),ExceptionsEnums.Talent.NO_DATA);
        AssertUtils.isFalse(talentPrivate.getUserId() == StpUtil.getLoginIdAsLong(),"不能内推非本人私库人才");

        SendTo sendTo = sendToService.getOne(new LambdaQueryWrapper<SendTo>().eq(SendTo::getAuditStatus,ConstantsEnums.YESNOWAIT.WAIT.getValue())
                .eq(SendTo::getFromId,StpUtil.getLoginIdAsLong())
                .eq(SendTo::getToId,reqVO.getUserId())
                .eq(SendTo::getPrivateId,reqVO.getPrivateId()));
        AssertUtils.isFalse(ObjectUtils.isEmpty(sendTo),"请勿重复操作");

        talentPrivate.setSendStatus(SendStatus.WAIT.getStatus());
        talentPrivateService.updateById(talentPrivate);

        sendTo = new SendTo();
        sendTo.setPrivateId(reqVO.getPrivateId());
        sendTo.setTalentName(talentInfoService.getById(talentPrivate.getInfoId()).getName());
        sendTo.setAuditStatus(ConstantsEnums.YESNOWAIT.WAIT.getValue());
        sendTo.setRemark(reqVO.getRemark());
        sendTo.setDept(RoleType.TALENT.code);
        sendTo.setFromId(StpUtil.getLoginIdAsLong());
        sendTo.setToId(reqVO.getUserId());
        sendTo.setCreateTime(TimeUtil.getNowWithSec());
        try {
            sendToService.save(sendTo);
        } catch (Exception e){
            e.printStackTrace();
            AssertUtils.throwException("操作失败");
        }
        return Result.success();
    }

    @Override
    public Result<GetSendHistoryRespVO> getSendHistory(GetSendHistoryReqVO reqVO){
        if (ObjectUtils.isNotEmpty(reqVO.getPageSize()))
            reqVO.setPageSize(Math.min(15,reqVO.getPageSize()));
        GetSendHistoryRespVO respVO = new GetSendHistoryRespVO();
        respVO.setList(sendToMapper.selectSendHistoryList(reqVO,StpUtil.getLoginIdAsLong()));
        respVO.setTotal(sendToMapper.selectSendHistoryNum(reqVO,StpUtil.getLoginIdAsLong()));
        return Result.success(respVO);
    }

    @Override
    public Result<GetSendHistoryRespVO> getSendAudit(GetSendAuditReqVO reqVO){
        if (ObjectUtils.isNotEmpty(reqVO.getPageSize()))
            reqVO.setPageSize(Math.min(15,reqVO.getPageSize()));
        GetSendHistoryRespVO respVO = new GetSendHistoryRespVO();
        respVO.setList(sendToMapper.selectAllSendTo(reqVO));
        respVO.setTotal(sendToMapper.selectAllSendToNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result auditSend(AuditSendReqVO reqVO) {
        String status = reqVO.getStatus();
        AssertUtils.isFalse("PASS".equals(status) || "FAIL".equals(status), ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        SendTo sendTo = sendToService.getOne(new LambdaQueryWrapper<SendTo>().eq(SendTo::getId, reqVO.getId())
                .eq(SendTo::getDlt, "NO"));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(sendTo), ExceptionsEnums.Common.NO_DATA);
        sendTo.setAuditStatus(status);
        sendTo.setAuditId(StpUtil.getLoginIdAsLong());
        sendTo.setAuditRemark(reqVO.getAuditRemark());
        sendTo.setAuditTime(TimeUtil.getNowWithSec());

        Long privateId = sendTo.getPrivateId();
        TalentPrivate talentPrivate = talentPrivateService.getById(privateId);
        talentPrivate.setSendStatus(SendStatus.NO.getStatus());

        if ("PASS".equals(status)) {
            if (!ConstantsEnums.YESNOWAIT.NO.getValue().equals(talentPrivate.getThrowback())) {
                sendTo.setAuditStatus(ConstantsEnums.AuditStatus.FAIL.getStatus());
                sendTo.setAuditRemark("内推失败，该人才已扔回公海或扔回公海待审核");
                sendToService.updateById(sendTo);
                return Result.success();
            }
            talentPrivate.setUserId(sendTo.getToId());
        }
        sendToService.updateById(sendTo);
        talentPrivateService.updateById(talentPrivate);
        return Result.success();
    }

    @Override
    public Result readyMatch(Long id){
        TalentPrivate talentPrivate = talentPrivateService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate),ExceptionsEnums.Talent.NO_DATA);
        AssertUtils.isFalse(talentPrivate.getStatus().equals(TalentPrivateStatus.FOLLOWUP),"只能操作跟进中人才");
        AssertUtils.isFalse(talentPrivate.getUserId() == StpUtil.getLoginIdAsLong(),ExceptionsEnums.Common.NO_PERMISSION);

        talentPrivate.setStatus(TalentPrivateStatus.MATCHING.getStatus());
        talentPrivateService.updateById(talentPrivate);

        TalentMatch talentMatch = new TalentMatch();
        talentMatch.setPrivateId(id);
        talentMatch.setCreateTime(TimeUtil.getNowWithSec());
        talentMatch.setStatus(TalentMatchStatus.READY.getStatus());
        talentMatchService.save(talentMatch);
        return Result.success();
    }


}
