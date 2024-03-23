package com.liepin.contract.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.service.base.UserService;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.BusinessException;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.contract.cache.RequireCache;
import com.liepin.contract.constant.ContractStatus;
import com.liepin.contract.entity.base.*;
import com.liepin.contract.entity.vo.list.*;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;
import com.liepin.contract.mapper.ContractMapper;
import com.liepin.contract.service.ContractService;
import com.liepin.contract.service.base.ContractAuditHistoryService;
import com.liepin.contract.service.base.ContractMatchService;
import com.liepin.contract.service.base.EnterpriseContractService;
import com.liepin.contract.service.base.impl.*;
import com.liepin.enterprise.constant.EnterprisePrivateStatus;
import com.liepin.enterprise.entity.base.EnterprisePrivate;
import com.liepin.enterprise.service.base.EnterprisePrivateService;
import com.liepin.talent.constant.TalentPrivateStatus;
import com.liepin.talent.entity.base.TalentInfo;
import com.liepin.talent.entity.base.TalentPrivate;
import com.liepin.talent.service.base.TalentInfoService;
import com.liepin.talent.service.base.TalentPrivateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final EnterpriseContractService enterpriseContractService;
    private final UserService userService;
    private final ContractMatchService contractMatchService;
    private final TalentPrivateService talentPrivateService;
    private final TalentInfoService talentInfoService;
    private final ContractMapper contractMapper;
    private final ContractAuditHistoryService contractAuditHistoryService;
    private final EnterprisePrivateService enterprisePrivateService;
    private final EnterpriseContractRequireServiceImpl enterpriseContractRequireService;
    private final RegisterMoneyHistoryServiceImpl registerMoneyHistoryService;
    private final EnterpriseContractMoneyApplyServiceImpl enterpriseContractMoneyApplyService;
    private final TalentContractMoneyApplyServiceImpl talentContractMoneyApplyService;
    private final PerformanceServiceImpl performanceService;
    private final RequireCache requireCache;

    @Autowired
    public ContractServiceImpl(EnterpriseContractService enterpriseContractService,UserService userService,
                               ContractMatchService contractMatchService,TalentPrivateService talentPrivateService,RequireCache requireCache,
                               ContractMapper contractMapper,ContractAuditHistoryService contractAuditHistoryService,TalentInfoService talentInfoService,
                               EnterprisePrivateService enterprisePrivateService,EnterpriseContractRequireServiceImpl enterpriseContractRequireService,
                               RegisterMoneyHistoryServiceImpl registerMoneyHistoryService,EnterpriseContractMoneyApplyServiceImpl enterpriseContractMoneyApplyService,
                               TalentContractMoneyApplyServiceImpl talentContractMoneyApplyService,PerformanceServiceImpl performanceService){
        this.performanceService = performanceService;
        this.talentContractMoneyApplyService = talentContractMoneyApplyService;
        this.enterpriseContractMoneyApplyService = enterpriseContractMoneyApplyService;
        this.registerMoneyHistoryService = registerMoneyHistoryService;
        this.requireCache = requireCache;
        this.talentInfoService = talentInfoService;
        this.enterpriseContractRequireService = enterpriseContractRequireService;
        this.enterprisePrivateService = enterprisePrivateService;
        this.contractAuditHistoryService = contractAuditHistoryService;
        this.contractMapper = contractMapper;
        this.talentPrivateService = talentPrivateService;
        this.contractMatchService = contractMatchService;
        this.userService = userService;
        this.enterpriseContractService = enterpriseContractService;
    }



    @Override
    public Result<GetContractsByPrivateIdRespVO> getContractsByPrivateId(Long id){
        GetContractsByPrivateIdRespVO respVO = new GetContractsByPrivateIdRespVO();
        List<GetContractsByPrivateIdListVO> list = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        LambdaQueryWrapper<EnterpriseContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseContract::getPrivateId,id)
                .eq(EnterpriseContract::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())
                .eq(EnterpriseContract::getUserId, StpUtil.getLoginIdAsLong());
        for (EnterpriseContract r : enterpriseContractService.list(queryWrapper)){
            GetContractsByPrivateIdListVO temp = new GetContractsByPrivateIdListVO();
            //temp.setContractPrice(new BigDecimal(r.getContractPrice()).divide(new BigDecimal(100)));
            temp.setId(r.getId());
            temp.setUserId(r.getUserId());
            temp.setName(userService.getById(r.getUserId()).getName());
            temp.setStatus(r.getStatus());
            temp.setCreateTime(r.getCreateTime());
            temp.setPriceType(r.getPriceType());
            list.add(temp);
            total = total.add(temp.getContractPrice());
        }

        respVO.setList(list);
        respVO.setTotalPrice(total);
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result newContract(NewContractReqVO reqVO){
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(reqVO.getPrivateId()) && ObjectUtils.isNotEmpty(reqVO.getPriceType())
            && ObjectUtils.isNotEmpty(reqVO.getTotalPrice()) && ObjectUtils.isNotEmpty(reqVO.getTotalRequireNum())
            && ObjectUtils.isNotEmpty(reqVO.getRequires()),
                ExceptionsEnums.Common.PARAM_LACK);
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(reqVO.getPrivateId());
        if (ObjectUtils.isEmpty(enterprisePrivate)
                || !enterprisePrivate.getThrowback().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                || enterprisePrivate.getStatus().equals(EnterprisePrivateStatus.CONTRACT.getStatus())){
            AssertUtils.throwException(ExceptionsEnums.Common.FAIL);
        }
        enterprisePrivate.setStatus(EnterprisePrivateStatus.CONTRACT.getStatus());
        enterprisePrivateService.updateById(enterprisePrivate);

        Long total = 0L;
        Long totalNum = 0L;
        Long totalOtherPrice = 0L;
        for (NewContractRequireListVO require : reqVO.getRequires()){
            AssertUtils.isFalse(ObjectUtils.isNotEmpty(require.getPrice())
                    && ObjectUtils.isNotEmpty(require.getRequireNum())
                    && ObjectUtils.isNotEmpty(require.getOtherPrice()),
                    ExceptionsEnums.Common.PARAM_LACK);
            total += require.getPrice().add(require.getOtherPrice()).multiply(new BigDecimal(100)).longValue();
            totalNum += require.getRequireNum();
            if (ObjectUtils.isNotEmpty(require.getOtherPrice())){
                totalOtherPrice += require.getOtherPrice().multiply(new BigDecimal(100)).longValue();
            }
        }
        if (reqVO.getTotalPrice().multiply(new BigDecimal(100)).longValue() != total)
            return Result.fail("合同价格不一致");
        if (!reqVO.getTotalRequireNum().equals(totalNum)){
            return Result.fail("证书需求数量不一致");
        }

        EnterpriseContract contract = new EnterpriseContract();
        BeanUtils.copyProperties(reqVO,contract);
        contract.setTotalPrice(total);
        contract.setTotalRequireNum(totalNum);
        Long userId  = StpUtil.getLoginIdAsLong();
        contract.setUserId(userId);
        contract.setCreateTime(TimeUtil.getNowWithSec());
        contract.setProfit(total - totalOtherPrice);
        enterpriseContractService.save(contract);

        List<EnterpriseContractRequire> requires = new ArrayList<>();
        for (NewContractRequireListVO require : reqVO.getRequires()){
            EnterpriseContractRequire temp = new EnterpriseContractRequire();
            BeanUtils.copyProperties(require,temp);
            temp.setPrivateId(contract.getPrivateId());
            temp.setUserId(userId);
            temp.setContractId(contract.getId());
            temp.setPrice(require.getPrice().multiply(new BigDecimal(100)).longValue());
            temp.setOtherPrice(require.getOtherPrice().multiply(new BigDecimal(100)).longValue());
            requires.add(temp);
        }

        enterpriseContractRequireService.saveBatch(requires);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteContract(Long id){
        EnterpriseContract contract = enterpriseContractService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
            && contract.getStatus().equals(ContractStatus.READY.getStatus())
            && (!StpUtil.getRoleList().contains(RoleType.ENTERPRISE.code) || contract.getUserId().equals(StpUtil.getLoginIdAsLong()))
            && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),
            ExceptionsEnums.Common.FAIL);

        contract.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
        enterpriseContractService.updateById(contract);
        EnterprisePrivate enterprisePrivate = enterprisePrivateService.getById(contract.getPrivateId());
        enterprisePrivate.setStatus(EnterprisePrivateStatus.FOLLOWUP.getStatus());
        enterprisePrivateService.updateById(enterprisePrivate);
        
        List<EnterpriseContractRequire> requires = enterpriseContractRequireService.list(
                new LambdaQueryWrapper<EnterpriseContractRequire>()
                        .eq(EnterpriseContractRequire::getContractId,id)
                        .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()));
        requires.forEach(require -> require.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue()));
        enterpriseContractRequireService.updateBatchById(requires);

        LambdaQueryWrapper<ContractMatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractMatch::getContractId,id)
                .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue());
        for (ContractMatch match : contractMatchService.list(queryWrapper)){
            match.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
            contractMatchService.updateById(match);
            TalentPrivate talentPrivate = talentPrivateService.getById(match.getTalentId());
            talentPrivate.setStatus(TalentPrivateStatus.FOLLOWUP.getStatus());
            talentPrivateService.updateById(talentPrivate);
        }
        requireCache.RemoveContractCache(id);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result SendAudit(Long id){
        EnterpriseContract enterpriseContract = enterpriseContractService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterpriseContract)
                && enterpriseContract.getDlt().equals("NO")
                && enterpriseContract.getStatus().equals(ContractStatus.READY.getStatus())
                ,ExceptionsEnums.Common.NO_DATA);

        List<EnterpriseContractRequire> requires = enterpriseContractRequireService
                .list(new LambdaQueryWrapper<EnterpriseContractRequire>()
                        .eq(EnterpriseContractRequire::getContractId,enterpriseContract.getId())
                        .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()));

        for (EnterpriseContractRequire require : requires){
            if (!require.getRequireNum().equals(require.getMatchedNum())){
                AssertUtils.throwException(ExceptionsEnums.Contract.LESS_NUM);
            }
            require.setStatus(ContractStatus.WAIT.getStatus());
            enterpriseContractRequireService.updateById(require);
            requireCache.Remove(require.getId());
        }

        enterpriseContract.setStatus(ContractStatus.WAIT.getStatus());
        enterpriseContractService.updateById(enterpriseContract);
        ContractAuditHistory auditHistory = new ContractAuditHistory();
        auditHistory.setContractId(enterpriseContract.getId());
        auditHistory.setStatus(ContractStatus.WAIT.getStatus());
        auditHistory.setUserId(enterpriseContract.getUserId());
        contractAuditHistoryService.save(auditHistory);
        requireCache.RemoveContractCache(id);
        return Result.success();
    }

    @Override
    public Result<GetContractAuditRespVO> getAudit(GetContractAuditReqVO reqVO){
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(reqVO.getStatus())
                && ObjectUtils.isNotEmpty(reqVO.getPage())
                && ObjectUtils.isNotEmpty(reqVO.getPageSize())
                && (reqVO.getStatus().equals(ContractStatus.PASS.getStatus())
                    || reqVO.getStatus().equals(ContractStatus.WAIT.getStatus())
                    || reqVO.getStatus().equals(ContractStatus.FAIL.getStatus())),
                ExceptionsEnums.Common.FAIL);
        Page<ContractAuditHistory> page = new Page<>(reqVO.getPage(), reqVO.getPageSize());
        LambdaQueryWrapper<ContractAuditHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractAuditHistory::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())
                    .eq(ContractAuditHistory::getStatus,reqVO.getStatus());
        if (StpUtil.getRoleList().contains(RoleType.ENTERPRISE.ENTERPRISE.code))
            queryWrapper.eq(ContractAuditHistory::getUserId,StpUtil.getLoginIdAsLong());

        contractAuditHistoryService.page(page,queryWrapper);
        GetContractAuditRespVO respVO = new GetContractAuditRespVO();
        List<GetContractInfoRespVO> list = new ArrayList<>();
        page.getRecords()
                .stream()
                .map(ContractAuditHistory::getContractId)
                .forEach(contractId -> {
                    Result<GetContractInfoRespVO> res = getContractInfo(contractId);
                    if (res.getCode() == 200)
                        list.add(res.getData());
                    else
                        AssertUtils.throwException(contractId + "号合同需求获取失败");
                });

        respVO.setList(list);
        respVO.setTotal(page.getTotal());
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result auditContract(AuditContractReqVO reqVO){
        Long contractId = reqVO.getContractId();
        String status = reqVO.getStatus();
        String remark = reqVO.getRemark();
        EnterpriseContract contract = enterpriseContractService.getById(contractId);

        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contractId)
                && ObjectUtils.isNotEmpty(status)
                && ObjectUtils.isNotEmpty(contract)
                && (status.equals(ContractStatus.PASS.getStatus()) || status.equals(ContractStatus.FAIL.getStatus()))
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && contract.getStatus().equals(ContractStatus.WAIT.getStatus()),
                ExceptionsEnums.Common.FAIL);

        ContractAuditHistory auditHistory = contractAuditHistoryService.getOne(new LambdaQueryWrapper<ContractAuditHistory>()
                .eq(ContractAuditHistory::getContractId,contractId)
                .eq(ContractAuditHistory::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())
                .eq(ContractAuditHistory::getStatus, ConstantsEnums.YESNOWAIT.WAIT.getValue()));
        auditHistory.setAuditTime(TimeUtil.getNowWithSec());
        auditHistory.setContractId(contractId);
        auditHistory.setAuditId(StpUtil.getLoginIdAsLong());
        auditHistory.setRemark(remark);
        auditHistory.setStatus(status);
        contractAuditHistoryService.updateById(auditHistory);

        Long profit = 0L;
        Long totalTalentPrice = 0L;
        Long totalOtherPrice = 0L;
        if (status.equals(ContractStatus.FAIL.getStatus())){
            contract.setStatus(ContractStatus.READY.getStatus());
            List<Long> ids = enterpriseContractRequireService.list(new LambdaQueryWrapper<EnterpriseContractRequire>()
                            .eq(EnterpriseContractRequire::getContractId,contractId)
                            .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()))
                    .stream()
                    .map(EnterpriseContractRequire::getId)
                    .collect(Collectors.toList());
            contractMapper.updateRequireStatusBatch(ids,ContractStatus.READY.getStatus());
        } else{
            contract.setStatus(ContractStatus.MATCHING.getStatus());
            List<EnterpriseContractRequire> requires = enterpriseContractRequireService.list(new LambdaQueryWrapper<EnterpriseContractRequire>()
                    .eq(EnterpriseContractRequire::getContractId,contractId)
                    .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()));
            List<Long> ids = requires
                    .stream()
                    .map(EnterpriseContractRequire::getId)
                    .collect(Collectors.toList());
            contractMapper.updateRequireStatusBatch(ids,ContractStatus.MATCHING.getStatus());
            List<Long> matches = contractMatchService.list(new LambdaQueryWrapper<ContractMatch>()
                            .eq(ContractMatch::getContractId,contractId)
                            .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()))
                    .stream()
                    .map(ContractMatch::getId)
                    .collect(Collectors.toList());
            contractMapper.updateMatchStatusBatch(matches,ContractStatus.MATCHING.getStatus());

            totalOtherPrice = requires
                    .stream()
                    .mapToLong(EnterpriseContractRequire::getOtherPrice)
                    .sum();
            totalTalentPrice = contractMatchService.list(new LambdaQueryWrapper<ContractMatch>()
                            .eq(ContractMatch::getContractId, contractId)
                            .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()))
                    .stream()
                    .mapToLong(ContractMatch::getTalentPrice)
                    .sum();
        }
        profit = contract.getTotalPrice() - totalTalentPrice - totalOtherPrice;
        AssertUtils.isFalse(profit > 0,"利润小于零");
        AssertUtils.isFalse(contract.getProfit().equals(profit),"利润错误");
        contract.setProfit(profit);
        enterpriseContractService.updateById(contract);
        requireCache.RemoveContractCache(contractId);
        return Result.success();
    }

    @Override
    public Result<GetContractsRespVO> getContracts(GetContractsReqVO reqVO){
        GetContractsRespVO respVO = new GetContractsRespVO();
        Page<EnterpriseContractRequire> page = new Page<>(reqVO.getPage(), reqVO.getPageSize());
        LambdaQueryWrapper<EnterpriseContractRequire> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseContractRequire::getStatus,ContractStatus.READY.getStatus())
                    .eq(ObjectUtils.isNotEmpty(reqVO.getPrivateId()),EnterpriseContractRequire::getPrivateId,reqVO.getPrivateId())
                    .eq(ObjectUtils.isNotEmpty(reqVO.getUserId()),EnterpriseContractRequire::getUserId,reqVO.getUserId())
                    .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue());
        enterpriseContractRequireService.page(page,queryWrapper);

        List<ContractRequireListVO> list = new ArrayList<>();
        page.getRecords().stream().forEach(require -> {
            Result<ContractRequireListVO> res = getRequireInfo(require.getId());
            if (res.getCode() == 200){
                list.add(res.getData());
            } else {
                AssertUtils.throwException(require.getId() + "号需求获取失败");
            }
        });
        respVO.setList(list);
        respVO.setTotal(page.getTotal());
        return Result.success(respVO);
    }

    @Override
    public Result<GetContractInfoRespVO> getContractInfo(Long contractId){
        GetContractInfoRespVO cache = requireCache.GetContract(contractId);
        if (ObjectUtils.isNotEmpty(cache))
            return Result.success(cache);

        GetContractInfoRespVO respVO = new GetContractInfoRespVO();
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract) && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                ,ExceptionsEnums.Common.NO_DATA);

        BeanUtils.copyProperties(contract,respVO);
        respVO.setTotalPrice(new BigDecimal((contract.getTotalPrice())).divide(new BigDecimal(100L)));
        respVO.setProfit(new BigDecimal(contract.getProfit()).divide(new BigDecimal(100L)));
        respVO.setRegisteredAmount(new BigDecimal(contract.getRegisteredAmount()).divide(new BigDecimal(100L)));
        respVO.setAmountOnContract(new BigDecimal(contract.getAmountOnContract()).divide(new BigDecimal(100L)));
        respVO.setUsedAmount(new BigDecimal(contract.getUsedAmount()).divide(new BigDecimal(100L)));
        if (ObjectUtils.isNotEmpty(respVO.getUserId()))
            respVO.setUsername(userService.getById(respVO.getUserId()).getName());
        if (ObjectUtils.isNotEmpty(respVO.getPrivateId()))
            respVO.setEnterpriseName(contractMapper.selectEnterpriseNameByPrivateId(respVO.getPrivateId()));

        LambdaQueryWrapper<EnterpriseContractRequire> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseContractRequire::getContractId,contractId)
                .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO);
        List<ContractRequireListVO> requires = new ArrayList<>();
        enterpriseContractRequireService.list(queryWrapper)
                .stream()
                .map(EnterpriseContractRequire::getId)
                .collect(Collectors.toList())
                .forEach(requireId -> {
                    Result<ContractRequireListVO> res = getRequireInfo(requireId);
                    if (res.getCode() == 200){
                        requires.add(res.getData());
                    } else {
                        log.info(res.getMsg());
                        AssertUtils.throwException(requireId + "号需求查询失败");
                    }
                });

        respVO.setRequires(requires);
        requireCache.SetContract(contractId,respVO);
        return Result.success(respVO);
    }

    @Override
    public Result<ContractRequireListVO> getRequireInfo(Long requireId){
        if (ObjectUtils.isEmpty(requireId) || requireId <= 0)
            return Result.fail();

        ContractRequireListVO cache = requireCache.Get(requireId);
        if (ObjectUtils.isNotEmpty(cache))
            return Result.success(cache);

        ContractRequireListVO res = new ContractRequireListVO();
        EnterpriseContractRequire require = enterpriseContractRequireService.getOne(new LambdaQueryWrapper<EnterpriseContractRequire>()
                .eq(EnterpriseContractRequire::getId,requireId)
                .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()));
        if (ObjectUtils.isEmpty(requireId))
            return Result.fail();

        BeanUtils.copyProperties(require,res);
        if (ObjectUtils.isNotEmpty(require.getPrivateId()))
            res.setEnterpriseName(contractMapper.selectEnterpriseNameByPrivateId(require.getPrivateId()));
        if (ObjectUtils.isNotEmpty(require.getUserId()))
            res.setUsername(userService.getById(require.getUserId()).getName());
        res.setPrice(new BigDecimal(require.getPrice()).divide(new BigDecimal(100)));

        List<ContractMatchInfo> matchInfo = new ArrayList<>();
        List<ContractMatch> matches = contractMatchService.list(new LambdaQueryWrapper<ContractMatch>()
                .eq(ContractMatch::getRequireId,requireId)
                .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()));
        for (ContractMatch match : matches){
            ContractMatchInfo info = new ContractMatchInfo();
            BeanUtils.copyProperties(match,info);
            info.setMatchId(match.getId());
            info.setUsername(userService.getById(info.getUserId()).getUsername());
            if (ObjectUtils.isNotEmpty(info.getTalentId()))
                info.setTalentName(contractMapper.selectTalentNameByPrivateId(info.getTalentId()));
            info.setTalentPrice(new BigDecimal(match.getTalentPrice()).divide(new BigDecimal(100)));
            info.setPaidPrice(new BigDecimal(match.getPaidPrice()).divide(new BigDecimal(100)));
            TalentPrivate talentPrivate = talentPrivateService.getById(match.getTalentId());
            TalentInfo talentInfo = talentInfoService.getById(talentPrivate.getInfoId());
            BeanUtils.copyProperties(talentInfo,info,"createTime");
            matchInfo.add(info);
        }

        res.setMatches(matchInfo);
        requireCache.Set(requireId,res);
        return Result.success(res);
    }

    @Override
    public Result<GetSelfContractRespVO> getSelfContract(GetSelfContractReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getStatus()),"状态不能为空");
        GetSelfContractRespVO respVO = new GetSelfContractRespVO();
        List<GetContractInfoRespVO> list = new ArrayList<>();
        Page<EnterpriseContract> page = new Page<>(reqVO.getPage(), reqVO.getPageSize());
        enterpriseContractService.page(page,
                new LambdaQueryWrapper<EnterpriseContract>()
                .eq(EnterpriseContract::getUserId,StpUtil.getLoginIdAsLong())
                .eq(EnterpriseContract::getStatus,reqVO.getStatus())
                .eq(EnterpriseContract::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()));

        page.getRecords()
                .stream()
                .map(EnterpriseContract::getId)
                .forEach(id -> {
                    Result<GetContractInfoRespVO> res = getContractInfo(id);
                    if (res.getCode() == 200)
                        list.add(res.getData());
                    else
                        AssertUtils.throwException("获取"+ id +"号合同失败");
                });

        respVO.setList(list);
        respVO.setTotal(page.getTotal());
        return Result.success(respVO);
    }

    @Override
    public Result uploadContract(UploadContractReqVO reqVO){
        Long contractId = reqVO.getContractId();
        String contractLink = reqVO.getContractLink();
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && ObjectUtils.isNotEmpty(contractId)
                && ObjectUtils.isNotEmpty(contractLink)
                && contract.getUserId() == StpUtil.getLoginIdAsLong()
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),
                ExceptionsEnums.Common.FAIL);

        contract.setContractLink(contractLink);
        contract.setUploadContract(ConstantsEnums.YESNOWAIT.YES.getValue());
        enterpriseContractService.updateById(contract);
        requireCache.RemoveContractCache(reqVO.getContractId());
        return Result.success();
    }

    @Override
    public Result<GetSelfMatchesRespVO> getSelfMatches(GetSelfMatchesReqVO reqVO){
        Long page = reqVO.getPage();
        Long pageSize = reqVO.getPageSize();
        String status = reqVO.getStatus();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(pageSize),ExceptionsEnums.Common.PARAM_LACK);
        GetSelfMatchesRespVO respVO = new GetSelfMatchesRespVO();
        List<GetSelfMatchesListVO> list = new ArrayList<>();
        Page<ContractMatch> matchPage = new Page<>(page,pageSize);
        contractMatchService.page(matchPage,new LambdaQueryWrapper<ContractMatch>()
                .eq(ContractMatch::getUserId,StpUtil.getLoginIdAsLong())
                .eq(ContractMatch::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue())
                .eq(ContractMatch::getStatus,status));

        matchPage.getRecords()
                .forEach(match -> {
                    Result<GetContractInfoRespVO> res = getContractInfo(match.getContractId());
                    if (res.getCode() == 200){
                        GetSelfMatchesListVO temp = new GetSelfMatchesListVO();
                        temp.setContract(res.getData());
                        temp.setContractId(match.getContractId());
                        temp.setMatchId(match.getId());
                        temp.setRequireId(match.getRequireId());
                        list.add(temp);
                    } else {
                        AssertUtils.throwException("获取"+ match.getContractId() +"号合同失败");
                    }
                });
        respVO.setList(list);
        respVO.setTotal(matchPage.getTotal());
        return Result.success(respVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result match(MatchReqVO reqVO){
        Long contractId = reqVO.getContractId();
        Long requireId = reqVO.getRequireId();

        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        EnterpriseContractRequire require = enterpriseContractRequireService.getById(requireId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                    && ObjectUtils.isNotEmpty(require)
                    && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                    && require.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                    && contract.getStatus().equals(ContractStatus.READY.getStatus()),
                    ExceptionsEnums.Common.NO_DATA);

        Long matchedNum = require.getMatchedNum();
        Long requireNum = require.getRequireNum();
        if (requireNum - matchedNum < reqVO.getTalents().size())
            return Result.fail("匹配人数过多");
        require.setMatchedNum(matchedNum + reqVO.getTalents().size());
        enterpriseContractRequireService.updateById(require);

        for (MatchTalent talent : reqVO.getTalents()){
            if (ObjectUtils.isEmpty(talent.getPrice()) || talent.getPrice().equals(new BigDecimal(0)))
                AssertUtils.throwException("人才费用不能为空");
            TalentPrivate talentPrivate = talentPrivateService.getById(talent.getId());
            ContractMatch match = contractMatchService.getOne(new LambdaQueryWrapper<ContractMatch>()
                    .eq(ContractMatch::getContractId,contractId)
                    .eq(ContractMatch::getRequireId,requireId)
                    .eq(ContractMatch::getTalentId,talent.getId()));
            AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate)
                    && talentPrivate.getStatus().equals(TalentPrivateStatus.FOLLOWUP.getStatus())
                    && talentPrivate.getThrowback().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                    && talentPrivate.getUserId() == StpUtil.getLoginIdAsLong()
                    && (ObjectUtils.isEmpty(match) || match.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())),
                    ExceptionsEnums.Common.FAIL);

            talentPrivate.setStatus(TalentPrivateStatus.MATCHING.getStatus());
            talentPrivateService.updateById(talentPrivate);

            ContractMatch contractMatch = new ContractMatch();
            contractMatch.setContractId(contractId);
            contractMatch.setRequireId(requireId);
            contractMatch.setUserId(StpUtil.getLoginIdAsLong());
            contractMatch.setTalentId(talent.getId());
            contractMatch.setTalentPrice(talent.getPrice().multiply(new BigDecimal(100)).longValue());
            contractMatch.setCreateTime(TimeUtil.getNowWithSec());
            contractMatchService.save(contractMatch);
            contract.setProfit(contract.getProfit() - talent.getPrice().multiply(new BigDecimal(100)).longValue());
            AssertUtils.isFalse(contract.getProfit() > 0, "合同利润小于零");
        }
        enterpriseContractService.updateById(contract);
        requireCache.Remove(reqVO.getRequireId());
        requireCache.RemoveContractCache(contractId);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancelMatch(CancelMatchReqVO reqVO){
        Long contractId = reqVO.getContractId();
        Long requireId = reqVO.getRequireId();
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        EnterpriseContractRequire require = enterpriseContractRequireService.getById(requireId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                        && ObjectUtils.isNotEmpty(require)
                        && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                        && require.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                        && contract.getStatus().equals(ContractStatus.READY.getStatus()),
                ExceptionsEnums.Common.NO_DATA);

        require.setMatchedNum(require.getMatchedNum() - reqVO.getTalentIds().size());
        enterpriseContractRequireService.updateById(require);

        for (Long talentId : reqVO.getTalentIds()){
            ContractMatch match = contractMatchService.getOne(new LambdaQueryWrapper<ContractMatch>()
                    .eq(ContractMatch::getContractId,contractId)
                    .eq(ContractMatch::getRequireId,require)
                    .eq(ContractMatch::getTalentId,talentId));
            if (ObjectUtils.isEmpty(match))
                continue;
            match.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
            contractMatchService.updateById(match);
            TalentPrivate talentPrivate = talentPrivateService.getById(talentId);
            talentPrivate.setStatus(TalentPrivateStatus.FOLLOWUP.getStatus());
            talentPrivateService.updateById(talentPrivate);
            contract.setProfit(contract.getProfit() + match.getTalentPrice());
        }
        enterpriseContractService.updateById(contract);
        requireCache.Remove(reqVO.getRequireId());
        requireCache.RemoveContractCache(reqVO.getContractId());
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result registerMoney(RegisterMoneyReqVO reqVO){
        Long contractId = reqVO.getContractId();
        BigDecimal money = reqVO.getMoney();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contractId) && ObjectUtils.isNotEmpty(money),
                ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract) && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
            && contract.getStatus().equals(ContractStatus.MATCHING.getStatus()),
                ExceptionsEnums.Common.FAIL);
        Long moneyLong = money.multiply(new BigDecimal(100)).longValue();
        AssertUtils.isFalse(moneyLong >= 0,"认款金额不能为负");

        RegisterMoneyHistory mostRecent = registerMoneyHistoryService.getOne(new LambdaQueryWrapper<RegisterMoneyHistory>()
                .eq(RegisterMoneyHistory::getContractId,contractId)
                .eq(RegisterMoneyHistory::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())
                .in(RegisterMoneyHistory::getStatus,"YES","WAIT")
                .orderByDesc(RegisterMoneyHistory::getCreateTime));

        if (ObjectUtils.isNotEmpty(mostRecent)){
            // 后续认款
            AssertUtils.isFalse(!mostRecent.getStatus().equals("WAIT"),"您的上一笔认款尚未审核完成");
            AssertUtils.isFalse(moneyLong <= mostRecent.getRestFromTotal(),"认款金额不能大于合同总款");

            RegisterMoneyHistory register = new RegisterMoneyHistory();
            register.setContractId(contractId);
            register.setUserId(StpUtil.getLoginIdAsLong());
            register.setAmount(moneyLong);
            register.setRestFromTotal(register.getRestFromTotal() - moneyLong);
            register.setCreateTime(TimeUtil.getNowWithSec());
            registerMoneyHistoryService.save(register);
        } else {
            // 首次认款
            Long contractProfit = contract.getProfit();
            AssertUtils.isFalse(moneyLong >= contractProfit,"首次认款金额必须大于合同利润");
            AssertUtils.isFalse(moneyLong <= contract.getTotalPrice(),"认款金额不能大于合同总款");

            RegisterMoneyHistory register = new RegisterMoneyHistory();
            register.setContractId(contractId);
            register.setUserId(StpUtil.getLoginIdAsLong());
            register.setAmount(moneyLong);
            register.setRestFromTotal(contract.getTotalPrice() - moneyLong);
            register.setCreateTime(TimeUtil.getNowWithSec());
            registerMoneyHistoryService.save(register);
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result auditMoney(AuditMoneyReqVO reqVO){
        Long registerId = reqVO.getRegisterId();
        String status = reqVO.getStatus();
        RegisterMoneyHistory register = registerMoneyHistoryService.getById(registerId);

        AssertUtils.isFalse(ObjectUtils.isNotEmpty(registerId) && ObjectUtils.isNotEmpty(status)
            && ObjectUtils.isNotEmpty(register) && register.getStatus().equals("WAIT") && (status.equals("PASS") || status.equals("FAIL")),
                ExceptionsEnums.Common.PARAMTER_IS_ERROR);

        EnterpriseContract contract = enterpriseContractService.getById(register.getContractId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                        && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                        && contract.getId().equals(reqVO.getContractId())
                        && (contract.getStatus().equals(ContractStatus.MATCHING.getStatus()) || contract.getStatus().equals(ContractStatus.UNFINISHED.getStatus())),
                ExceptionsEnums.Common.FAIL);

        if (status.equals("PASS")){
            register.setStatus("PASS");
            register.setAuditId(StpUtil.getLoginIdAsLong());
            register.setAuditTime(TimeUtil.getNowWithSec());
            register.setRemark(reqVO.getRemark());
            registerMoneyHistoryService.updateById(register);

            if (contract.getRegisteredAmount() == 0){
                contract.setAmountOnContract(register.getAmount() - contract.getProfit());
            } else {
                contract.setAmountOnContract(contract.getAmountOnContract() + register.getAmount());
            }
            contract.setRegisteredAmount(contract.getRegisteredAmount() + register.getAmount());
            if (contract.getTotalPrice().equals(contract.getRegisteredAmount())){
                contract.setStatus(ContractStatus.FINISHED.getStatus());
            } else {
                contract.setStatus(ContractStatus.UNFINISHED.getStatus());
            }
            enterpriseContractService.updateById(contract);
        } else {
            register.setStatus("FAIL");
            register.setAuditId(StpUtil.getLoginIdAsLong());
            register.setAuditTime(TimeUtil.getNowWithSec());
            register.setRemark(reqVO.getRemark());
            registerMoneyHistoryService.updateById(register);
        }
        requireCache.RemoveContractCache(register.getContractId());
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result sharePerformance(Long contractId){
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),
                ExceptionsEnums.Common.FAIL);
        List<String> role = StpUtil.getRoleList();
        AssertUtils.isFalse(role.contains(RoleType.MANAGER.code) || contract.getUserId().equals(StpUtil.getLoginIdAsLong()),
                ExceptionsEnums.Common.NO_PERMISSION);
        AssertUtils.isFalse(contract.getPerformanceShared().equals(ConstantsEnums.YESNOWAIT.YES.getValue()),
                "不可重复分业绩");
        AssertUtils.isFalse(contract.getStatus().equals(ContractStatus.FINISHED.getStatus()) || contract.getStatus().equals(ContractStatus.UNFINISHED.getStatus()),
                "完结或未完结状态才可进行分业绩");
        List<ContractMatch> matches = contractMatchService.list(new LambdaQueryWrapper<ContractMatch>()
                .eq(ContractMatch::getContractId,contractId)
                .eq(ContractMatch::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue()));
        contract.setPerformanceShared(ConstantsEnums.YESNOWAIT.YES.getValue());
        enterpriseContractService.updateById(contract);

        Performance enterprisePerformance = new Performance();
        enterprisePerformance.setUserId(contract.getUserId());
        enterprisePerformance.setRole(RoleType.ENTERPRISE.code);
        enterprisePerformance.setPerformanceAmount(contract.getProfit() / 2);
        enterprisePerformance.setContractId(contractId);
        enterprisePerformance.setEnterprisePrivateId(contract.getPrivateId());
        enterprisePerformance.setCreateTime(TimeUtil.getNowWithSec());
        performanceService.save(enterprisePerformance);

        Long talentProfit = contract.getProfit() / 2;
        Long total = matches
                .stream()
                .map(ContractMatch::getTalentPrice)
                .mapToLong(Long::longValue)
                .sum();
        for (ContractMatch match : matches){
            Long profit = talentProfit * (match.getTalentPrice() / total);
            Performance talentPerformance = new Performance();
            talentPerformance.setUserId(match.getUserId());
            talentPerformance.setRole(RoleType.TALENT.code);
            talentPerformance.setPerformanceAmount(profit);
            talentPerformance.setContractId(contractId);
            talentPerformance.setRequireId(match.getRequireId());
            talentPerformance.setMatchId(match.getId());
            talentPerformance.setTalentPrivateId(match.getTalentId());
            talentPerformance.setCreateTime(TimeUtil.getNowWithSec());
            performanceService.save(talentPerformance);
            requireCache.Remove(match.getRequireId());
        }
        requireCache.RemoveContractCache(contractId);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<GetRegisterMoneyAuditRespVO> getRegisterMoneyAudit(GetRegisterMoneyAuditReqVO reqVO){
        Long page = reqVO.getPage();
        Long pageSize = reqVO.getPageSize();
        String status = reqVO.getStatus();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(status)
                && ObjectUtils.isNotEmpty(page)
                && ObjectUtils.isNotEmpty(pageSize)
                ,ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        Page<RegisterMoneyHistory> historyPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<RegisterMoneyHistory> wrapper = new LambdaQueryWrapper<RegisterMoneyHistory>();
        wrapper.eq(RegisterMoneyHistory::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue())
                .eq(RegisterMoneyHistory::getStatus,status)
                .orderByDesc(RegisterMoneyHistory::getCreateTime);
        if (StpUtil.getRoleList().contains(RoleType.ENTERPRISE.code)){
            wrapper.eq(RegisterMoneyHistory::getUserId,StpUtil.getLoginIdAsLong());
        }
        registerMoneyHistoryService.page(historyPage,wrapper);
        GetRegisterMoneyAuditRespVO respVO = new GetRegisterMoneyAuditRespVO();
        List<GetRegisterMoneyAuditListVO> list = new ArrayList<>();
        historyPage.getRecords()
                .forEach(register -> {
                    GetRegisterMoneyAuditListVO vo = new GetRegisterMoneyAuditListVO();
                    Result<GetContractInfoRespVO> result = getContractInfo(register.getContractId());
                    if (result.getCode() == 200){
                        vo.setContract(result.getData());
                        BeanUtils.copyProperties(register,vo);
                        vo.setUserName(userService.getById(register.getUserId()).getName());
                        vo.setAuditName(userService.getById(register.getAuditId()).getName());
                        vo.setRestFromTotal(new BigDecimal(register.getRestFromTotal()));
                        vo.setAmount(new BigDecimal(register.getAmount()));
                    } else {
                        AssertUtils.throwException("获取"+ register.getContractId() +"号合同失败");
                    }
                    list.add(vo);
                });
        respVO.setList(list);
        respVO.setTotal(historyPage.getTotal());
        return Result.success(respVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result enterpriseApplyMoney(ApplyMoneyReqVO reqVO){
        Long contractId = reqVO.getContractId();
        BigDecimal money = reqVO.getMoney();
        String usage = reqVO.getUsage();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contractId) && ObjectUtils.isNotEmpty(money)
                && ObjectUtils.isNotEmpty(usage) && money.compareTo(BigDecimal.ZERO) > 0,
                ExceptionsEnums.Common.PARAMTER_IS_ERROR);

        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(contract.getUserId().equals(StpUtil.getLoginIdAsLong()),ExceptionsEnums.Common.NO_PERMISSION);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && !contract.getStatus().equals(ContractStatus.READY.getStatus())
                && !contract.getStatus().equals(ContractStatus.WAIT.getStatus()),
                ExceptionsEnums.Common.FAIL);
        EnterpriseContractMoneyApply mostRecent = enterpriseContractMoneyApplyService.getOne(new LambdaQueryWrapper<EnterpriseContractMoneyApply>()
                .eq(EnterpriseContractMoneyApply::getContractId,contractId)
                .eq(EnterpriseContractMoneyApply::getUserId,StpUtil.getLoginIdAsLong())
                .eq(EnterpriseContractMoneyApply::getStatus,"WAIT")
                .eq(EnterpriseContractMoneyApply::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue()));
        AssertUtils.isFalse(ObjectUtils.isEmpty(mostRecent),"等待上次申请审核完成");

        Long moneyLong = money.longValue();
        AssertUtils.isFalse(contract.getAmountOnContract() >= moneyLong,"申请金额大于合同剩余金额");

        EnterpriseContractMoneyApply apply = new EnterpriseContractMoneyApply();
        apply.setContractId(contractId);
        apply.setUserId(StpUtil.getLoginIdAsLong());
        apply.setApplyNum(moneyLong);
        apply.setUsage(usage);
        apply.setCreateTime(TimeUtil.getNowWithSec());
        enterpriseContractMoneyApplyService.save(apply);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result talentApplyMoney(TalentApplyMoneyReqVO reqVO){
        Long contractId = reqVO.getContractId();
        Long matchId = reqVO.getMatchId();
        BigDecimal money = reqVO.getMoney();
        String usage = reqVO.getUsage();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contractId) && ObjectUtils.isNotEmpty(matchId)
                && ObjectUtils.isNotEmpty(money) && ObjectUtils.isNotEmpty(usage) && money.compareTo(BigDecimal.ZERO) > 0,
                ExceptionsEnums.Common.PARAMTER_IS_ERROR);

        ContractMatch match = contractMatchService.getById(matchId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(match)
                && match.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && match.getUserId().equals(StpUtil.getLoginIdAsLong())
                && match.getContractId().equals(contractId)
                && !match.getStatus().equals(ContractStatus.FINISHED.getStatus()),
            ExceptionsEnums.Common.FAIL);

        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && !contract.getStatus().equals(ContractStatus.READY.getStatus())
                && !contract.getStatus().equals(ContractStatus.WAIT.getStatus()),
                ExceptionsEnums.Common.FAIL);

        TalentContractMoneyApply mostRecent = talentContractMoneyApplyService.getOne(new LambdaQueryWrapper<TalentContractMoneyApply>()
                .eq(TalentContractMoneyApply::getContractId,contractId)
                .eq(TalentContractMoneyApply::getMatchId,matchId)
                .eq(TalentContractMoneyApply::getUserId,StpUtil.getLoginIdAsLong())
                .eq(TalentContractMoneyApply::getStatus,"WAIT")
                .eq(TalentContractMoneyApply::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue()));
        AssertUtils.isFalse(ObjectUtils.isEmpty(mostRecent),"等待上次申请审核完成");

        Long moneyLong = money.longValue();
        AssertUtils.isFalse(contract.getAmountOnContract() >= moneyLong,"申请金额大于合同剩余金额");
        AssertUtils.isFalse(match.getPaidPrice() + moneyLong <= match.getTalentPrice(),"申请金额大于人才价格");
        TalentContractMoneyApply apply = new TalentContractMoneyApply();
        apply.setContractId(contractId);
        apply.setRequireId(match.getRequireId());
        apply.setMatchId(matchId);
        apply.setUserId(StpUtil.getLoginIdAsLong());
        apply.setApplyNum(moneyLong);
        apply.setUsage(usage);
        apply.setCreateTime(TimeUtil.getNowWithSec());
        talentContractMoneyApplyService.save(apply);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<GetEnterpriseApplyMoneyAuditRespVO> getEnterpriseApplyMoneyAudit(GetApplyMoneyAuditReqVO reqVO){
        Long page = reqVO.getPage();
        Long pageSize = reqVO.getPageSize();
        String status = reqVO.getStatus();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(pageSize)
                && ObjectUtils.isNotEmpty(status),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        Page<EnterpriseContractMoneyApply> applyPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<EnterpriseContractMoneyApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseContractMoneyApply::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue())
                .eq(EnterpriseContractMoneyApply::getStatus,status)
                .orderByDesc(EnterpriseContractMoneyApply::getCreateTime);
        if (StpUtil.getRoleList().contains(RoleType.ENTERPRISE.code)){
            queryWrapper.eq(EnterpriseContractMoneyApply::getUserId,StpUtil.getLoginIdAsLong());
        }
        enterpriseContractMoneyApplyService.page(applyPage,queryWrapper);

        GetEnterpriseApplyMoneyAuditRespVO respVO = new GetEnterpriseApplyMoneyAuditRespVO();
        List<GetEnterpriseApplyMoneyAuditListVO> list = new ArrayList<>();
        applyPage.getRecords()
                .forEach(apply -> {
                    Result<GetContractInfoRespVO> result = getContractInfo(apply.getContractId());
                    AssertUtils.isFalse(result.getCode() == 200,"获取"+apply.getContractId()+"号合同信息失败");

                    GetEnterpriseApplyMoneyAuditListVO vo = new GetEnterpriseApplyMoneyAuditListVO();
                    BeanUtils.copyProperties(apply,vo);
                    vo.setContract(result.getData());
                    vo.setApplyNum(new BigDecimal(apply.getApplyNum()));
                    vo.setAuditName(userService.getById(apply.getAuditId()).getName());
                    vo.setUserName(userService.getById(apply.getUserId()).getName());
                    list.add(vo);
                });
        respVO.setList(list);
        respVO.setTotal(applyPage.getTotal());
        return Result.success(respVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<GetTalentApplyMoneyAuditRespVO> getTalentApplyMoneyAudit(GetApplyMoneyAuditReqVO reqVO){
        Long page = reqVO.getPage();
        Long pageSize = reqVO.getPageSize();
        String status = reqVO.getStatus();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(pageSize)
                && ObjectUtils.isNotEmpty(status),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        Page<TalentContractMoneyApply> applyPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<TalentContractMoneyApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TalentContractMoneyApply::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue())
                .eq(TalentContractMoneyApply::getStatus,status)
                .orderByDesc(TalentContractMoneyApply::getCreateTime);
        if (StpUtil.getRoleList().contains(RoleType.TALENT.code)){
            queryWrapper.eq(TalentContractMoneyApply::getUserId,StpUtil.getLoginIdAsLong());
        }
        talentContractMoneyApplyService.page(applyPage,queryWrapper);

        GetTalentApplyMoneyAuditRespVO respVO = new GetTalentApplyMoneyAuditRespVO();
        List<GetTalentApplyMoneyAuditListVO> list = new ArrayList<>();
        applyPage.getRecords()
                .forEach(apply -> {
                    Result<GetContractInfoRespVO> result = getContractInfo(apply.getContractId());
                    AssertUtils.isFalse(result.getCode() == 200,"获取"+apply.getContractId()+"号合同信息失败");

                    GetTalentApplyMoneyAuditListVO vo = new GetTalentApplyMoneyAuditListVO();
                    BeanUtils.copyProperties(apply,vo);
                    vo.setContract(result.getData());
                    vo.setApplyNum(new BigDecimal(apply.getApplyNum()));
                    vo.setAuditName(userService.getById(apply.getAuditId()).getName());
                    vo.setUserName(userService.getById(apply.getUserId()).getName());
                    list.add(vo);
                });

        respVO.setList(list);
        respVO.setTotal(applyPage.getTotal());
        return Result.success(respVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result auditEnterpriseApply(AuditApplyReqVO reqVO){
        Long contractId = reqVO.getContractId();
        Long applyId = reqVO.getApplyId();
        String status = reqVO.getStatus();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contractId) && ObjectUtils.isNotEmpty(applyId)
                && ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(reqVO.getContractId())
                && (status.equals("PASS") || status.equals("FAIL")),
                ExceptionsEnums.Common.PARAMTER_IS_ERROR);

        EnterpriseContractMoneyApply apply = enterpriseContractMoneyApplyService.getById(applyId);
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && ObjectUtils.isNotEmpty(apply)
                && apply.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && apply.getContractId().equals(contractId),
                ExceptionsEnums.Common.FAIL);

        if (status.equals("PASS")){
            apply.setAuditId(StpUtil.getLoginIdAsLong());
            apply.setAuditTime(TimeUtil.getNowWithSec());
            apply.setStatus("PASS");
            enterpriseContractMoneyApplyService.updateById(apply);
            AssertUtils.isFalse(contract.getAmountOnContract() >= apply.getApplyNum(),"申请金额大于合同剩余金额");
            contract.setAmountOnContract(contract.getAmountOnContract() - apply.getApplyNum());
            contract.setUsedAmount(contract.getUsedAmount() + apply.getApplyNum());
            enterpriseContractService.updateById(contract);
        } else {
            apply.setAuditId(StpUtil.getLoginIdAsLong());
            apply.setAuditTime(TimeUtil.getNowWithSec());
            apply.setStatus("FAIL");
            enterpriseContractMoneyApplyService.updateById(apply);
        }
        requireCache.RemoveContractCache(contractId);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result auditTalentApply(AuditApplyReqVO reqVO){
        Long contractId = reqVO.getContractId();
        Long applyId = reqVO.getApplyId();
        String status = reqVO.getStatus();
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contractId) && ObjectUtils.isNotEmpty(applyId)
                && ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(reqVO.getContractId())
                && (status.equals("PASS") || status.equals("FAIL")),
                ExceptionsEnums.Common.PARAMTER_IS_ERROR);

        TalentContractMoneyApply apply = talentContractMoneyApplyService.getById(applyId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(apply) && apply.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && apply.getContractId().equals(contractId),
                ExceptionsEnums.Common.FAIL);

        ContractMatch match = contractMatchService.getById(apply.getMatchId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(match) && match.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),
                ExceptionsEnums.Common.FAIL);

        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && apply.getContractId().equals(contractId),
                ExceptionsEnums.Common.FAIL);

        if (status.equals("PASS")){
            AssertUtils.isFalse(contract.getAmountOnContract() >= apply.getApplyNum(),"申请金额大于合同剩余金额");
            apply.setAuditId(StpUtil.getLoginIdAsLong());
            apply.setAuditTime(TimeUtil.getNowWithSec());
            apply.setStatus("PASS");
            talentContractMoneyApplyService.updateById(apply);

            AssertUtils.isFalse(match.getPaidPrice() + apply.getApplyNum() <= match.getTalentPrice(),"申请金额大于人才价格");
            match.setPaidPrice(match.getPaidPrice() + apply.getApplyNum());
            if (match.getPaidPrice().equals(match.getTalentPrice())) {
                match.setStatus(ContractStatus.FINISHED.getStatus());
            } else {
                match.setStatus(ContractStatus.UNFINISHED.getStatus());
            }
            contractMatchService.updateById(match);

            contract.setAmountOnContract(contract.getAmountOnContract() - apply.getApplyNum());
            contract.setUsedAmount(contract.getUsedAmount() + apply.getApplyNum());
            enterpriseContractService.updateById(contract);
        } else {
            apply.setAuditId(StpUtil.getLoginIdAsLong());
            apply.setAuditTime(TimeUtil.getNowWithSec());
            apply.setStatus("FAIL");
            talentContractMoneyApplyService.updateById(apply);
        }
        requireCache.RemoveContractCache(contractId);
        requireCache.Remove(match.getRequireId());
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reDoEnterprise(Long contractId){
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && contract.getStatus().equals(ContractStatus.FINISHED.getStatus()),
                ExceptionsEnums.Common.FAIL);
        AssertUtils.isFalse(contract.getUserId().equals(StpUtil.getLoginIdAsLong()),ExceptionsEnums.Common.NO_PERMISSION);

        EnterprisePrivate privateInfo = enterprisePrivateService.getById(contract.getPrivateId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(privateInfo),ExceptionsEnums.Enterprise.NO_DATA);
        privateInfo.setStatus(EnterprisePrivateStatus.FOLLOWUP.getStatus());
        enterprisePrivateService.updateById(privateInfo);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reDoTalent(Long matchId){
        ContractMatch match = contractMatchService.getById(matchId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(match)
                && match.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                && match.getStatus().equals(ContractStatus.FINISHED.getStatus()),
                ExceptionsEnums.Common.FAIL);
        AssertUtils.isFalse(match.getUserId().equals(StpUtil.getLoginIdAsLong()),ExceptionsEnums.Common.NO_PERMISSION);

        TalentPrivate privateInfo = talentPrivateService.getById(match.getTalentId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(privateInfo),ExceptionsEnums.Talent.NO_DATA);
        privateInfo.setStatus(TalentPrivateStatus.FOLLOWUP.getStatus());
        talentPrivateService.updateById(privateInfo);
        return Result.success();
    }
}
