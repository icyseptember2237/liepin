package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetTalentApplyMoneyAuditListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetTalentApplyMoneyAuditRespVO {
    private List<GetTalentApplyMoneyAuditListVO> list;
    private Long total;
}
