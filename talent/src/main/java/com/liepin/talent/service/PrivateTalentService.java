package com.liepin.talent.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.talent.entity.vo.req.*;
import com.liepin.talent.entity.vo.resp.FollowupInfoRespVO;
import com.liepin.talent.entity.vo.resp.GetFollowupRespVO;
import com.liepin.talent.entity.vo.resp.GetNotContactRespVO;
import com.liepin.talent.entity.vo.resp.GetSendHistoryRespVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PrivateTalentService {
    Result<GetNotContactRespVO> getNotContact(GetNotContactReqVO reqVO);

    Result<GetAuditRespVO> managerGetAudit(GetAuditReqVO reqVO);

    Result audit(AuditReqVO reqVO);

    Result throwback(ThrowbackReqVO reqVO);

    Result<GetAuditRespVO> getSelfAudit(GetAuditReqVO reqVO);

    Result addTalent(AddTalentReqVO reqVO);

    Result<FollowupInfoRespVO> followupInfo(Long id);

    Result followupTalent(FollowupTalentReqVO reqVO);

    Result<GetFollowupRespVO> getFollowup(GetFollowupReqVO reqVO);

    Result<GetFollowupRespVO> getAllFollowup();

    Result sendTo(SendToReqVO reqVO);

    Result<GetSendHistoryRespVO> getSendHistory(GetSendHistoryReqVO reqVO);

    Result<GetSendHistoryRespVO> getSendAudit(GetSendAuditReqVO reqVO);

    Result auditSend(AuditSendReqVO reqVO);

    Result readyMatch(Long id);
}
