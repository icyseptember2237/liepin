package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.ContractRequireListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetContractsRespVO {
    private List<ContractRequireListVO> list;
    private Long total;
}
