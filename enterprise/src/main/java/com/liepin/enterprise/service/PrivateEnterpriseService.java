package com.liepin.enterprise.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.*;
import com.liepin.enterprise.entity.vo.resp.FollowupInfoRespVO;
import com.liepin.enterprise.entity.vo.resp.GetFollowupRespVO;
import com.liepin.enterprise.entity.vo.resp.GetNotContactRespVO;
import com.liepin.enterprise.entity.vo.resp.GetAuditRespVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PrivateEnterpriseService {
    Result<GetNotContactRespVO> getNotContact(GetNotContactReqVO reqVO);

    Result<GetAuditRespVO> managerGetAudit(GetAuditReqVO reqVO);

    Result audit(AuditReqVO reqVO);

    Result throwback(ThrowbackReqVO reqVO);

    Result<GetAuditRespVO> getSelfAudit(GetAuditReqVO reqVO);

    Result addEnterprise(AddEnterpriseReqVO reqVO);

    Result<FollowupInfoRespVO> followupInfo(Long id);

    Result followupEnterprise(FollowupEnterpriseReqVO reqVO);

    Result<GetFollowupRespVO> getFollowup(GetFollowupReqVO reqVO);

    Result sendTo(SendToReqVO reqVO);
}
