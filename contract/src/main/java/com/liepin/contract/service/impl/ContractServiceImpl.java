package com.liepin.contract.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.service.base.UserService;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.contract.constant.ContractStatus;
import com.liepin.contract.entity.base.ContractAuditHistory;
import com.liepin.contract.entity.base.ContractMatch;
import com.liepin.contract.entity.base.EnterpriseContract;
import com.liepin.contract.entity.vo.list.*;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;
import com.liepin.contract.mapper.ContractMapper;
import com.liepin.contract.service.ContractService;
import com.liepin.contract.service.base.ContractAuditHistoryService;
import com.liepin.contract.service.base.ContractMatchService;
import com.liepin.contract.service.base.EnterpriseContractService;
import com.liepin.enterprise.service.base.EnterprisePrivateService;
import com.liepin.talent.constant.TalentPrivateStatus;
import com.liepin.talent.entity.base.TalentPrivate;
import com.liepin.talent.service.base.TalentPrivateService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final EnterpriseContractService enterpriseContractService;
    private final UserService userService;
    private final ContractMatchService contractMatchService;
    private final TalentPrivateService talentPrivateService;
    private final ContractMapper contractMapper;
    private final ContractAuditHistoryService contractAuditHistoryService;
    private final EnterprisePrivateService enterprisePrivateService;

    @Autowired
    public ContractServiceImpl(EnterpriseContractService enterpriseContractService,UserService userService,
                               ContractMatchService contractMatchService,TalentPrivateService talentPrivateService,
                               ContractMapper contractMapper,ContractAuditHistoryService contractAuditHistoryService,
                               EnterprisePrivateService enterprisePrivateService){
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
            temp.setContractPrice(new BigDecimal(r.getContractPrice()).divide(new BigDecimal(100)));
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
        for (NewContractListVO vo : reqVO.getList()){
            EnterpriseContract temp = new EnterpriseContract();
            AssertUtils.isFalse(ObjectUtils.isNotEmpty(vo.getPrivateId())
                    && ObjectUtils.isNotEmpty(vo.getContractPrice())
                    && vo.getPrivateId() > 0
                    && vo.getContractPrice().intValue() > 0,
                    ExceptionsEnums.Common.FAIL);
            BeanUtils.copyProperties(vo,temp);
            temp.setUserId(StpUtil.getLoginIdAsLong());
            temp.setContractPrice(vo.getContractPrice().longValue() * 100L);
            temp.setCreateTime(TimeUtil.getNowWithSec());
            enterpriseContractService.save(temp);
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result deleteContract(Long id){
        EnterpriseContract contract = enterpriseContractService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
            && contract.getStatus().equals(ContractStatus.READY.getStatus())
            && contract.getUserId().equals(StpUtil.getLoginIdAsLong())
            && contract.getStatus().equals(ConstantsEnums.YESNOWAIT.NO.getValue()),
            ExceptionsEnums.Common.FAIL);

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
        return Result.success();
    }

    @Override
    @Transactional
    public Result SendAudit(Long id){
        EnterpriseContract enterpriseContract = enterpriseContractService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(enterpriseContract)
                && enterpriseContract.getDlt().equals("NO")
                && enterpriseContract.getStatus().equals(ContractStatus.READY.getStatus()), ExceptionsEnums.Common.NO_DATA);
        Long matchedNum = contractMatchService.count(new LambdaQueryWrapper<ContractMatch>()
                .eq(ContractMatch::getContractId,id)
                .eq(ContractMatch::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue()));
        AssertUtils.isFalse(matchedNum == enterpriseContract.getRequireNum(),ExceptionsEnums.Contract.LESS_NUM);

        enterpriseContract.setStatus(ContractStatus.WAIT.getStatus());
        enterpriseContractService.updateById(enterpriseContract);
        ContractAuditHistory auditHistory = new ContractAuditHistory();
        auditHistory.setContractId(enterpriseContract.getId());
        auditHistory.setStatus(ContractStatus.WAIT.getStatus());
        auditHistory.setUserId(enterpriseContract.getUserId());
        contractAuditHistoryService.save(auditHistory);
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
        List<ContractAuditHistory> contracts = page.getRecords().stream()
                .distinct()
                .collect(Collectors.toList());

        List<GetAuditListVO> list = new ArrayList<>();
        contracts.stream().forEach(contract -> {
            GetAuditListVO temp = new GetAuditListVO();
            BeanUtils.copyProperties(contract,temp,"id","dlt");
            if (temp.getAuditId() != null && temp.getAuditId() > 0){
                temp.setAuditName(userService.getById(temp.getAuditId()).getName());
                temp.setUsername(userService.getById(temp.getUserId()).getName());
            } else {
                temp.setAuditId(null);
            }
            temp.setUsername(userService.getById(temp.getUserId()).getName());


            List<ContractMatchInfo> infos = contractMatchService.list(new LambdaQueryWrapper<ContractMatch>()
                            .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO)
                            .eq(ContractMatch::getContractId,temp.getContractId()))
                    .stream()
                    .map(match -> {
                        ContractMatchInfo info = new ContractMatchInfo();
                        BeanUtils.copyProperties(match,info);
                        info.setUsername(userService.getById(match.getUserId()).getName());
                        info.setTalentName(contractMapper.selectTalentNameByPrivateId(match.getTalentId()));
                        return info;
                    }).collect(Collectors.toList());
            temp.setMatchInfos(infos);

            EnterpriseContract c = enterpriseContractService.getById(temp.getContractId());
            temp.setPrivateId(c.getPrivateId());
            temp.setEnterpriseName(contractMapper.selectEnterpriseNameByPrivateId(temp.getPrivateId()));
            temp.setUploadContract(c.getUploadContract());
            temp.setCreateTime(c.getCreateTime());
            list.add(temp);
        });
        respVO.setTotal(new Long(contracts.size()));
        respVO.setList(list);
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
                .eq(ContractAuditHistory::getDlt, ConstantsEnums.YESNOWAIT.WAIT.getValue())
                .eq(ContractAuditHistory::getContractId,contractId));
        auditHistory.setAuditTime(TimeUtil.getNowWithSec());
        auditHistory.setContractId(contractId);
        auditHistory.setAuditId(StpUtil.getLoginIdAsLong());
        auditHistory.setRemark(remark);
        auditHistory.setStatus(status);
        contractAuditHistoryService.save(auditHistory);
        if (status.equals(ContractStatus.FAIL.getStatus()))
            contract.setStatus(ContractStatus.READY.getStatus());
        else
            contract.setStatus(status);
        enterpriseContractService.updateById(contract);
        return Result.success();
    }

    @Override
    public Result<GetContractsRespVO> getContracts(GetContractsReqVO reqVO){
        List<GetContractsListVO> respList = new ArrayList<>();
        Page<EnterpriseContract> page = new Page<>(reqVO.getPage(), reqVO.getPageSize());
        LambdaQueryWrapper<EnterpriseContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseContract::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())
                    .eq(EnterpriseContract::getStatus,ContractStatus.READY.getStatus())
                    .eq(ObjectUtils.isNotEmpty(reqVO.getPrivateId()),EnterpriseContract::getPrivateId,reqVO.getPrivateId())
                    .eq(ObjectUtils.isNotEmpty(reqVO.getUserId()),EnterpriseContract::getUserId,reqVO.getUserId());
        enterpriseContractService.page(page,queryWrapper);
        page.getRecords().stream().forEach(contract -> {
            GetContractsListVO temp = new GetContractsListVO();
            BeanUtils.copyProperties(contract,temp);
            temp.setEnterpriseName(contractMapper.selectEnterpriseNameByPrivateId(contract.getPrivateId()));
            temp.setContractPrice(new BigDecimal(contract.getContractPrice()).divide(new BigDecimal(100L)));
            temp.setMatchNum(contractMatchService.count(new LambdaQueryWrapper<ContractMatch>()
                    .eq(ContractMatch::getContractId,contract.getId())
                    .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())));
            respList.add(temp);
        });
        GetContractsRespVO respVO = new GetContractsRespVO();
        respVO.setList(respList);
        respVO.setTotal(page.getTotal());
        return Result.success(respVO);
    }

    @Override
    public Result<GetContractInfoRespVO> getContractInfo(Long contractId){
        GetContractInfoRespVO respVO = new GetContractInfoRespVO();
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract),ExceptionsEnums.Common.NO_DATA);

        BeanUtils.copyProperties(contract,respVO);
        respVO.setContractPrice(new BigDecimal((contract.getContractPrice())).divide(new BigDecimal(100L)));
        respVO.setUsername(userService.getById(respVO.getUserId()).getUsername());
        System.out.println(respVO);
        respVO.setEnterpriseName(contractMapper.selectEnterpriseNameByPrivateId(respVO.getPrivateId()));
        LambdaQueryWrapper<ContractMatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO)
                .eq(ContractMatch::getContractId,contractId);

        List<ContractMatchInfo> list = contractMatchService.list(queryWrapper).stream()
                .map(match -> {
                   ContractMatchInfo info = new ContractMatchInfo();
                   BeanUtils.copyProperties(match,info);
                   info.setUsername(userService.getById(match.getUserId()).getName());
                   info.setTalentName(contractMapper.selectTalentNameByPrivateId(match.getTalentId()));
                   return info;
                }).collect(Collectors.toList());
        respVO.setMatches(list);
        return Result.success(respVO);
    }

    @Override
    public Result<GetSelfContractRespVO> getSelfContract(GetSelfContractReqVO reqVO){
        GetSelfContractRespVO respVO = new GetSelfContractRespVO();
        List<GetContractInfoRespVO> list = new ArrayList<>();
        enterpriseContractService.list(
                new LambdaQueryWrapper<EnterpriseContract>()
                .eq(EnterpriseContract::getDlt, ConstantsEnums.YESNOWAIT.NO)
                .eq(EnterpriseContract::getUserId,StpUtil.getLoginIdAsLong()))
                .stream().map(EnterpriseContract::getId)
                .collect(Collectors.toList()).stream()
                .forEach(contractId -> {
                    list.add(getContractInfo(contractId).getData());
                });
        respVO.setList(list);
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
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result match(MatchReqVO reqVO){
        Long contractId = reqVO.getContractId();

        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                    && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                    && contract.getStatus().equals(ContractStatus.READY.getStatus()),
                    ExceptionsEnums.Common.NO_DATA);
        Long matchedNum = contractMatchService.count(new LambdaQueryWrapper<ContractMatch>()
                .eq(ContractMatch::getContractId,contractId)
                .eq(ContractMatch::getDlt,ConstantsEnums.YESNOWAIT.NO.getValue()));
        Long needNum = contract.getRequireNum() - matchedNum;
        if (needNum < reqVO.getTalentIds().size())
            return Result.fail("匹配人数过多, 需求人数: " + needNum);

        for (Long talentId : reqVO.getTalentIds()){
            TalentPrivate talentPrivate = talentPrivateService.getById(talentId);
            ContractMatch match = contractMatchService.getOne(new LambdaQueryWrapper<ContractMatch>()
                    .eq(ContractMatch::getDlt, ConstantsEnums.YESNOWAIT.NO.getValue())
                    .eq(ContractMatch::getContractId,contractId)
                    .eq(ContractMatch::getTalentId,talentId));
            AssertUtils.isFalse(ObjectUtils.isNotEmpty(talentPrivate)
                    && talentPrivate.getStatus().equals(TalentPrivateStatus.FOLLOWUP.getStatus())
                    && talentPrivate.getThrowback().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                    && talentPrivate.getUserId() == StpUtil.getLoginIdAsLong()
                    && ObjectUtils.isEmpty(match),
                    ExceptionsEnums.Common.FAIL);

            talentPrivate.setStatus(TalentPrivateStatus.MATCHING.getStatus());
            talentPrivateService.updateById(talentPrivate);

            ContractMatch contractMatch = new ContractMatch();
            contractMatch.setContractId(contractId);
            contractMatch.setUserId(StpUtil.getLoginIdAsLong());
            contractMatch.setTalentId(talentId);
            contractMatch.setCreateTime(TimeUtil.getNowWithSec());
            contractMatchService.save(contractMatch);
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancelMatch(CancelMatchReqVO reqVO){
        Long contractId = reqVO.getContractId();
        EnterpriseContract contract = enterpriseContractService.getById(contractId);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(contract)
                        && contract.getDlt().equals(ConstantsEnums.YESNOWAIT.NO.getValue())
                        && contract.getStatus().equals(ContractStatus.READY.getStatus()),
                ExceptionsEnums.Common.NO_DATA);
        for (Long talentId : reqVO.getTalentIds()){
            ContractMatch match = contractMatchService.getOne(new LambdaQueryWrapper<ContractMatch>()
                    .eq(ContractMatch::getContractId,contractId)
                    .eq(ContractMatch::getTalentId,talentId));
            if (ObjectUtils.isEmpty(match))
                continue;
            match.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
            contractMatchService.updateById(match);
            TalentPrivate talentPrivate = talentPrivateService.getById(talentId);
            talentPrivate.setStatus(TalentPrivateStatus.FOLLOWUP.getStatus());
            talentPrivateService.updateById(talentPrivate);
        }
        return Result.success();
    }
}
