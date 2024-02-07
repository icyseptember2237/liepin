package com.liepin.contract.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;

public interface ContractService {

    Result<GetContractsByPrivateIdRespVO> getContractsByPrivateId(Long id);

    Result newContract(NewContractReqVO reqVO);

    Result deleteContract(Long id);

    Result SendAudit(Long id);

    Result<GetContractAuditRespVO> getAudit(GetContractAuditReqVO reqVO);

    Result auditContract(AuditContractReqVO reqVO);

    Result<GetContractsRespVO> getContracts(GetContractsReqVO reqVO);

    Result<GetContractInfoRespVO> getContractInfo(Long contractId);

    Result<GetSelfContractRespVO> getSelfContract(GetSelfContractReqVO reqVO);

    Result uploadContract(UploadContractReqVO reqVO);

    Result match(MatchReqVO reqVO);

    Result cancelMatch(CancelMatchReqVO reqVO);

}
