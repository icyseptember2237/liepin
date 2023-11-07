package com.liepin.enterprise.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.*;
import com.liepin.enterprise.entity.vo.resp.GetNotContactRespVO;
import com.liepin.enterprise.entity.vo.resp.GetAuditRespVO;

public interface PrivateEnterpriseService {
    Result<GetNotContactRespVO> getNotContact(GetNotContactReqVO reqVO);

    Result<GetAuditRespVO> managerGetAudit(GetAuditReqVO reqVO);

    Result audit(AuditReqVO reqVO);

    Result throwback(ThrowbackReqVO reqVO);

    Result<GetAuditRespVO> getSelfAudit(GetAuditReqVO reqVO);

    Result addEnterprise(AddEnterpriseReqVO reqVO);
}
