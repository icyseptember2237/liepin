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
import com.liepin.contract.entity.base.ContractAuditHistory;
import com.liepin.contract.entity.base.ContractMatch;
import com.liepin.contract.entity.base.EnterpriseContract;
import com.liepin.contract.entity.base.EnterpriseContractRequire;
import com.liepin.contract.entity.vo.list.*;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;
import com.liepin.contract.mapper.ContractMapper;
import com.liepin.contract.service.ContractService;
import com.liepin.contract.service.base.ContractAuditHistoryService;
import com.liepin.contract.service.base.ContractMatchService;
import com.liepin.contract.service.base.EnterpriseContractService;
import com.liepin.contract.service.base.impl.EnterpriseContractRequireServiceImpl;
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
    private final RequireCache requireCache;

    @Autowired
    public ContractServiceImpl(EnterpriseContractService enterpriseContractService,UserService userService,
                               ContractMatchService contractMatchService,TalentPrivateService talentPrivateService,RequireCache requireCache,
                               ContractMapper contractMapper,ContractAuditHistoryService contractAuditHistoryService,TalentInfoService talentInfoService,
                               EnterprisePrivateService enterprisePrivateService,EnterpriseContractRequireServiceImpl enterpriseContractRequireService){
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
        for (NewContractRequireListVO require : reqVO.getRequires()){
            AssertUtils.isFalse(ObjectUtils.isNotEmpty(require.getPrice())
                    && ObjectUtils.isNotEmpty(require.getRequireNum())
                    && ObjectUtils.isNotEmpty(require.getOtherPrice()),
                    ExceptionsEnums.Common.PARAM_LACK);
            total += require.getPrice().add(require.getOtherPrice()).multiply(new BigDecimal(100)).longValue();
            totalNum += require.getRequireNum();
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
                .distinct()
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
        contractAuditHistoryService.save(auditHistory);

        if (status.equals(ContractStatus.FAIL.getStatus())){
            contract.setStatus(ContractStatus.READY.getStatus());
            enterpriseContractRequireService.list(new LambdaQueryWrapper<EnterpriseContractRequire>()
                            .eq(EnterpriseContractRequire::getContractId,contractId)
                            .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()))
                    .forEach(require -> {
                        require.setStatus(ContractStatus.READY.getStatus());
                        enterpriseContractRequireService.updateById(require);
                        requireCache.Remove(require.getId());
                    });
        } else{
            contract.setStatus(ContractStatus.MATCHING.getStatus());
            enterpriseContractRequireService.list(new LambdaQueryWrapper<EnterpriseContractRequire>()
                    .eq(EnterpriseContractRequire::getContractId,contractId)
                    .eq(EnterpriseContractRequire::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue()))
                    .forEach(require -> {
                        require.setStatus(ContractStatus.MATCHING.getStatus());
                        enterpriseContractRequireService.updateById(require);
                        requireCache.Remove(require.getId());
                    });
        }
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
        if (ObjectUtils.isNotEmpty(respVO.getUserId()))
            respVO.setUsername(userService.getById(respVO.getUserId()).getUsername());
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
            info.setUsername(userService.getById(info.getUserId()).getUsername());
            if (ObjectUtils.isNotEmpty(info.getTalentId()))
                info.setTalentName(contractMapper.selectTalentNameByPrivateId(info.getTalentId()));
            info.setTalentPrice(new BigDecimal(match.getTalentPrice()).divide(new BigDecimal(100)));
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
        }

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
        }
        requireCache.Remove(reqVO.getRequireId());
        requireCache.RemoveContractCache(reqVO.getContractId());
        return Result.success();
    }
}
