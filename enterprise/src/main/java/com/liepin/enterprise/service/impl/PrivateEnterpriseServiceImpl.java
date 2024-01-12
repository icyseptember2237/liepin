package com.liepin.enterprise.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.entity.base.User;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.enterprise.constant.EnterprisePrivateStatus;
import com.liepin.enterprise.entity.base.*;
import com.liepin.enterprise.entity.vo.req.*;
import com.liepin.enterprise.entity.vo.resp.*;
import com.liepin.enterprise.mapper.EnterpriseMapper;
import com.liepin.enterprise.mapper.PrivateEnterpriseMapper;
import com.liepin.enterprise.mapper.base.SendToMapper;
import com.liepin.enterprise.service.PrivateEnterpriseService;
import com.liepin.enterprise.service.base.impl.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivateEnterpriseServiceImpl implements PrivateEnterpriseService {

    private final PrivateEnterpriseMapper privateEnterpriseMapper;

    private final EnterprisePrivateServiceImpl enterprisePrivateService;

    private final EnterpriseInfoServiceImpl enterpriseInfoService;

    private final EnterpriseThrowbackHistoryServiceImpl enterpriseThrowbackHistoryService;

    private final EnterprisePrivateFollowupServiceImpl enterprisePrivateFollowupService;

    private final EnterpriseMapper enterpriseMapper;

    private final SendToServiceImpl sendToService;

    private final SendToMapper sendToMapper;

    @Autowired
    public PrivateEnterpriseServiceImpl(PrivateEnterpriseMapper privateEnterpriseMapper,EnterprisePrivateServiceImpl enterprisePrivateService,
                                        EnterpriseInfoServiceImpl enterpriseInfoService,EnterpriseThrowbackHistoryServiceImpl enterpriseThrowbackHistoryService,
                                        EnterprisePrivateFollowupServiceImpl enterprisePrivateFollowupService,EnterpriseMapper enterpriseMapper,
                                        SendToServiceImpl sendToService,SendToMapper sendToMapper){
        this.sendToMapper = sendToMapper;
        this.sendToService = sendToService;
        this.enterpriseMapper = enterpriseMapper;
        this.enterpriseThrowbackHistoryService = enterpriseThrowbackHistoryService;
        this.enterpriseInfoService = enterpriseInfoService;
        this.enterprisePrivateService = enterprisePrivateService;
        this.privateEnterpriseMapper = privateEnterpriseMapper;
        this.enterprisePrivateFollowupService = enterprisePrivateFollowupService;
    }

    @Override
    public Result<GetNotContactRespVO> getNotContact(GetNotContactReqVO reqVO){
        GetNotContactRespVO respVO = new GetNotContactRespVO();
        respVO.setList(privateEnterpriseMapper.selectNotContactList(StpUtil.getLoginIdAsLong(),reqVO));
        respVO.setTotal(privateEnterpriseMapper.selectNotContactListNum(StpUtil.getLoginIdAsLong(),reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<GetAuditRespVO> managerGetAudit(GetAuditReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getAuditStatus()),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        GetAuditRespVO respVO = new GetAuditRespVO();
        respVO.setList(privateEnterpriseMapper.selectAuditList(reqVO,null));
        respVO.setTotal(privateEnterpriseMapper.selectAuditListNum(reqVO,null));
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result audit(AuditReqVO reqVO){

        EnterpriseThrowbackHistory history = enterpriseThrowbackHistoryService.getById(reqVO.getId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(history),ExceptionsEnums.Common.NO_DATA);
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(history.getPrivateId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterprisePrivate),ExceptionsEnums.Common.NO_DATA);
        EnterpriseInfo info = enterpriseInfoService.getById(history.getInfoId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),ExceptionsEnums.Common.NO_DATA);

        if (ConstantsEnums.AuditStatus.PASS.getStatus().equals(reqVO.getStatus())){
            history.setAuditStatus(ConstantsEnums.AuditStatus.PASS.getStatus());
            history.setAuditTime(TimeUtil.getNowWithSec());
            enterpriseThrowbackHistoryService.updateById(history);

            enterprisePrivate.setThrowback(ConstantsEnums.YESNOWAIT.YES.getValue());
            enterprisePrivateService.updateById(enterprisePrivate);

            info.setIsPrivate(ConstantsEnums.YESNOWAIT.NO.getValue());
            enterpriseInfoService.updateById(info);
            return Result.success();
        } else if (ConstantsEnums.AuditStatus.FAIL.getStatus().equals(reqVO.getStatus())){
            history.setAuditStatus(ConstantsEnums.AuditStatus.FAIL.getStatus());
            history.setAuditTime(TimeUtil.getNowWithSec());
            enterpriseThrowbackHistoryService.updateById(history);

            enterprisePrivate.setThrowback(ConstantsEnums.YESNOWAIT.NO.getValue());
            enterprisePrivateService.updateById(enterprisePrivate);
            return Result.success();
        }
        AssertUtils.throwException(ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        return null;
    }

    @Override
    @Transactional
    public Result throwback(ThrowbackReqVO reqVO){
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getOne(new LambdaQueryWrapper<EnterprisePrivate>().eq(EnterprisePrivate::getId,reqVO.getId())
                .eq(EnterprisePrivate::getThrowback,ConstantsEnums.YESNOWAIT.NO));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterprisePrivate),ExceptionsEnums.Enterprise.NO_DATA);
        AssertUtils.isFalse( StpUtil.getLoginIdAsLong() == enterprisePrivate.getUserId(),
                ExceptionsEnums.Common.NO_PERMISSION);
        AssertUtils.isFalse(ConstantsEnums.YESNOWAIT.NO.getValue().equals(enterprisePrivate.getThrowback()),
                "重复操作");

        enterprisePrivate.setThrowback(ConstantsEnums.YESNOWAIT.WAIT.getValue());
        enterprisePrivateService.updateById(enterprisePrivate);

        EnterpriseThrowbackHistory history = new EnterpriseThrowbackHistory();
        history.setInfoId(enterprisePrivate.getInfoId());
        history.setPrivateId(enterprisePrivate.getId());
        history.setCreateTime(TimeUtil.getNowWithSec());
        history.setThrowbackReason(reqVO.getThrowbackReason());
        history.setRemark(reqVO.getRemark());
        enterpriseThrowbackHistoryService.save(history);

        return Result.success();
    }

    @Override
    public Result<GetAuditRespVO> getSelfAudit(GetAuditReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getAuditStatus()),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        GetAuditRespVO respVO = new GetAuditRespVO();
        respVO.setList(privateEnterpriseMapper.selectAuditList(reqVO,StpUtil.getLoginIdAsLong()));
        respVO.setTotal(privateEnterpriseMapper.selectAuditListNum(reqVO,StpUtil.getLoginIdAsLong()));
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result addEnterprise(AddEnterpriseReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getName()),
                "单位名称不能为空");
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getEmail()) || StringUtils.isNotEmpty(reqVO.getPhone()),
                "联系方式不能为空");

        try {
            EnterpriseInfo info = new EnterpriseInfo();
            BeanUtils.copyProperties(reqVO,info);
            info.setIsPrivate(ConstantsEnums.YESNOWAIT.YES.getValue());
            info.setCreateTime(TimeUtil.getNowWithSec());
            enterpriseInfoService.save(info);

            EnterprisePrivate enterprisePrivate = new EnterprisePrivate();
            enterprisePrivate.setInfoId(info.getId());
            enterprisePrivate.setUserId(StpUtil.getLoginIdAsLong());
            enterprisePrivate.setCreateTime(TimeUtil.getNowWithSec());
            enterprisePrivate.setStatus(EnterprisePrivateStatus.NOT_CONTACT.getStatus());
            enterprisePrivateService.save(enterprisePrivate);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            AssertUtils.throwException(ExceptionsEnums.Enterprise.INSERT_FAIL);
        }

        return Result.success();
    }

    @Override
    public Result<FollowupInfoRespVO> followupInfo(Long id){
        FollowupInfoRespVO respVO = new FollowupInfoRespVO();
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterprisePrivate) && enterprisePrivate.getThrowback().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),ExceptionsEnums.Enterprise.NO_DATA);
        EnterpriseInfo info = enterpriseInfoService.getById(enterprisePrivateService.getById(id).getInfoId());

        BeanUtils.copyProperties(info,respVO);
        respVO.setList(enterpriseMapper.getFollowupHistory(id,1,5));
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result followupEnterprise(FollowupEnterpriseReqVO reqVO){
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getOne(new LambdaQueryWrapper<EnterprisePrivate>().eq(EnterprisePrivate::getId,reqVO.getId())
                .eq(EnterprisePrivate::getThrowback,ConstantsEnums.YESNOWAIT.NO));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterprisePrivate),ExceptionsEnums.Enterprise.NO_DATA);
        AssertUtils.isFalse(enterprisePrivate.getUserId() == StpUtil.getLoginIdAsLong(),ExceptionsEnums.Common.NO_PERMISSION);

        if (enterprisePrivate.getStatus().equals(EnterprisePrivateStatus.NOT_CONTACT.getStatus())){
            enterprisePrivate.setStatus(EnterprisePrivateStatus.FOLLOWUP.getStatus());
            enterprisePrivateService.updateById(enterprisePrivate);
        }

        EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
        BeanUtils.copyProperties(reqVO,enterpriseInfo,"id");
        enterpriseInfo.setId(enterprisePrivate.getInfoId());
        enterpriseInfoService.updateById(enterpriseInfo);

        EnterprisePrivateFollowup followup = new EnterprisePrivateFollowup();
        followup.setPrivateId(enterprisePrivate.getId());
        followup.setFollowupId(StpUtil.getLoginIdAsLong());
        followup.setTime(TimeUtil.getNowWithSec());
        followup.setRemark(reqVO.getFollowupRemark());
        followup.setNextTime(reqVO.getNextTime());
        enterprisePrivateFollowupService.save(followup);

        return Result.success();

    }

    @Override
    public Result<GetFollowupRespVO> getFollowup(GetFollowupReqVO reqVO){
        if (ObjectUtils.isNotEmpty(reqVO.getPageSize()))
            reqVO.setPageSize(Math.min(15,reqVO.getPageSize()));
        GetFollowupRespVO respVO = new GetFollowupRespVO();
        List<GetFollowupListVO> list = privateEnterpriseMapper.selectFollowupList(reqVO,StpUtil.getLoginIdAsLong());
        respVO.setList(list);
        respVO.setTotal(privateEnterpriseMapper.selectFollowupListNum(reqVO,StpUtil.getLoginIdAsLong()));
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result sendTo(SendToReqVO reqVO){
        AssertUtils.isFalse(StpUtil.getLoginIdAsLong() != reqVO.getUserId(),"不能内推给本人");
        AssertUtils.isFalse(sendToMapper.checkDept(RoleType.ENTERPRISE.code, reqVO.getUserId()) == 1,"只能内推至部门内部");
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getOne(new LambdaQueryWrapper<EnterprisePrivate>()
                .eq(EnterprisePrivate::getId,reqVO.getPrivateId())
                .eq(EnterprisePrivate::getThrowback,ConstantsEnums.YESNOWAIT.NO));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterprisePrivate),ExceptionsEnums.Enterprise.NO_DATA);
        AssertUtils.isFalse(enterprisePrivate.getUserId() == StpUtil.getLoginIdAsLong(),"不能内推非本人私库单位");

        SendTo sendTo = sendToService.getOne(new LambdaQueryWrapper<SendTo>().eq(SendTo::getAuditStatus,ConstantsEnums.YESNOWAIT.WAIT.getValue())
                .eq(SendTo::getFromId,StpUtil.getLoginIdAsLong())
                .eq(SendTo::getToId,reqVO.getUserId())
                .eq(SendTo::getPrivateId,reqVO.getPrivateId()));
        AssertUtils.isFalse(ObjectUtils.isEmpty(sendTo),"请勿重复操作");

        sendTo = new SendTo();
        sendTo.setPrivateId(reqVO.getPrivateId());
        sendTo.setEnterpriseName(enterpriseInfoService.getById(enterprisePrivate.getInfoId()).getName());
        sendTo.setAuditStatus(ConstantsEnums.YESNOWAIT.WAIT.getValue());
        sendTo.setRemark(reqVO.getRemark());
        sendTo.setDept(RoleType.ENTERPRISE.code);
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
    public Result auditSend(AuditSendReqVO reqVO){
        String status = reqVO.getStatus();
        AssertUtils.isFalse("PASS".equals(status) || "FAIL".equals(status),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        SendTo sendTo = sendToService.getOne(new LambdaQueryWrapper<SendTo>().eq(SendTo::getId,reqVO.getId())
                .eq(SendTo::getDlt,"NO"));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(sendTo),ExceptionsEnums.Common.NO_DATA);
        sendTo.setAuditStatus(status);
        sendTo.setAuditId(StpUtil.getLoginIdAsLong());
        sendTo.setAuditRemark(reqVO.getAuditRemark());
        sendTo.setAuditTime(TimeUtil.getNowWithSec());

        if ("PASS".equals(status)){
            Long privateId = sendTo.getPrivateId();
            EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(privateId);
            if (!ConstantsEnums.YESNOWAIT.NO.getValue().equals(enterprisePrivate.getThrowback())){
                sendTo.setAuditStatus(ConstantsEnums.AuditStatus.FAIL.getStatus());
                sendTo.setAuditRemark("审核失败，该单位已扔回公海或扔回公海待审核");
                sendToService.updateById(sendTo);
                return Result.success();
            }
            enterprisePrivate.setUserId(sendTo.getToId());
            sendToService.updateById(sendTo);
            enterprisePrivateService.updateById(enterprisePrivate);
        }
        return Result.success();
    }
}
