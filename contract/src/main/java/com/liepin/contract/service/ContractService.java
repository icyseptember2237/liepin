package com.liepin.contract.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.contract.entity.vo.list.ContractRequireListVO;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ContractService {

    Result<GetContractsByPrivateIdRespVO> getContractsByPrivateId(Long id);

    Result newContract(NewContractReqVO reqVO);

    Result deleteContract(Long id);

    Result SendAudit(Long id);

    Result<GetContractAuditRespVO> getAudit(GetContractAuditReqVO reqVO);

    Result auditContract(AuditContractReqVO reqVO);

    Result<GetContractsRespVO> getContracts(GetContractsReqVO reqVO);

    Result<GetContractInfoRespVO> getContractInfo(Long contractId);

    Result<ContractRequireListVO> getRequireInfo(Long requireId);

    Result<GetSelfContractRespVO> getSelfContract(GetSelfContractReqVO reqVO);

    Result uploadContract(UploadContractReqVO reqVO);

    Result<GetSelfMatchesRespVO> getSelfMatches(GetSelfMatchesReqVO reqVO);

    Result match(MatchReqVO reqVO);

    Result cancelMatch(CancelMatchReqVO reqVO);

    Result registerMoney(RegisterMoneyReqVO reqVO);

    Result auditMoney(AuditMoneyReqVO reqVO);

    Result sharePerformance(Long contractId);

    Result<GetRegisterMoneyAuditRespVO> getRegisterMoneyAudit(GetRegisterMoneyAuditReqVO reqVO);

    Result enterpriseApplyMoney(ApplyMoneyReqVO reqVO);

    Result talentApplyMoney(TalentApplyMoneyReqVO reqVO);

    Result<GetEnterpriseApplyMoneyAuditRespVO> getEnterpriseApplyMoneyAudit(GetApplyMoneyAuditReqVO reqVO);

    Result<GetTalentApplyMoneyAuditRespVO> getTalentApplyMoneyAudit(GetApplyMoneyAuditReqVO reqVO);

    Result auditEnterpriseApply(AuditApplyReqVO reqVO);

    Result auditTalentApply(AuditApplyReqVO reqVO);

    Result reDoEnterprise(Long contractId);

    Result reDoTalent(Long matchId);

}
