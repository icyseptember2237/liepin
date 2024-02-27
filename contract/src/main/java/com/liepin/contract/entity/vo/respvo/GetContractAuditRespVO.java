package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetAuditListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetContractAuditRespVO {
    private List<GetContractInfoRespVO> list;
    private Long total;
}
