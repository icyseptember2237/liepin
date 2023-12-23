package com.liepin.enterprise.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.enterprise.constant.EnterprisePrivateStatus;
import com.liepin.enterprise.entity.base.EnterpriseInfo;
import com.liepin.enterprise.entity.base.EnterprisePrivate;
import com.liepin.enterprise.entity.base.EnterprisePrivateFollowup;
import com.liepin.enterprise.entity.base.EnterpriseThrowbackHistory;
import com.liepin.enterprise.entity.vo.req.*;
import com.liepin.enterprise.entity.vo.resp.*;
import com.liepin.enterprise.mapper.EnterpriseMapper;
import com.liepin.enterprise.mapper.PrivateEnterpriseMapper;
import com.liepin.enterprise.service.PrivateEnterpriseService;
import com.liepin.enterprise.service.base.impl.EnterpriseInfoServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterprisePrivateFollowupServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterprisePrivateServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterpriseThrowbackHistoryServiceImpl;
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

import java.util.List;

@Service
public class PrivateEnterpriseServiceImpl implements PrivateEnterpriseService {

    private final PrivateEnterpriseMapper privateEnterpriseMapper;

    private final EnterprisePrivateServiceImpl enterprisePrivateService;

    private final EnterpriseInfoServiceImpl enterpriseInfoService;

    private final EnterpriseThrowbackHistoryServiceImpl enterpriseThrowbackHistoryService;

    private final EnterprisePrivateFollowupServiceImpl enterprisePrivateFollowupService;

    private final EnterpriseMapper enterpriseMapper;

    @Autowired
    public PrivateEnterpriseServiceImpl(PrivateEnterpriseMapper privateEnterpriseMapper,EnterprisePrivateServiceImpl enterprisePrivateService,
                                        EnterpriseInfoServiceImpl enterpriseInfoService,EnterpriseThrowbackHistoryServiceImpl enterpriseThrowbackHistoryService,
                                        EnterprisePrivateFollowupServiceImpl enterprisePrivateFollowupService,EnterpriseMapper enterpriseMapper){
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
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(reqVO.getId());
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
        EnterpriseInfo info = enterpriseInfoService.getById(enterprisePrivateService.getById(id).getInfoId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),ExceptionsEnums.Enterprise.NO_DATA);

        BeanUtils.copyProperties(info,respVO);
        respVO.setList(enterpriseMapper.getFollowupHistory(id,1,5));
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result followupEnterprise(FollowupEnterpriseReqVO reqVO){
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(reqVO.getId());
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
        GetFollowupRespVO respVO = new GetFollowupRespVO();
        List<GetFollowupListVO> list = privateEnterpriseMapper.selectFollowupList(reqVO);
        return Result.success(respVO);
    }

}
