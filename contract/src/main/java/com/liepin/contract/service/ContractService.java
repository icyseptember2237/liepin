package com.liepin.contract.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.contract.entity.vo.reqvo.*;
import com.liepin.contract.entity.vo.respvo.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ContractService {

    Result<GetContractsByPrivateIdRespVO> getContractsByPrivateId(Long id);

    Result newContract(NewContractReqVO reqVO);

    Result deleteContract(Long id);

    Result SendAudit(Long id);

    Result<GetAuditRespVO> getAudit(GetAuditReqVO reqVO);

    Result auditContract(AuditContractReqVO reqVO);

    Result<GetContractsRespVO> getContracts(GetContractsReqVO reqVO);

    Result<GetContractInfoRespVO> getContractInfo(Long contractId);

    Result<GetSelfContractRespVO> getSelfContract();

    Result uploadContract(UploadContractReqVO reqVO);

    Result match(MatchReqVO reqVO);

    Result cancelMatch(CancelMatchReqVO reqVO);

}
