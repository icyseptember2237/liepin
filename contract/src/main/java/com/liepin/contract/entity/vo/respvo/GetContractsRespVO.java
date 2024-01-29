package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetContractsListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetContractsRespVO {
    private List<GetContractsListVO> list;
    private Long total;
}
