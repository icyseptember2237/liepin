package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetEnterpriseApplyMoneyAuditListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetEnterpriseApplyMoneyAuditRespVO {
    private List<GetEnterpriseApplyMoneyAuditListVO> list;
    private Long total;
}
