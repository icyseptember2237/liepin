package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetRegisterMoneyAuditListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetRegisterMoneyAuditRespVO {
    List<GetRegisterMoneyAuditListVO> list;
    Long total;
}
