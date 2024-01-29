package com.liepin.contract.entity.vo.respvo;

import lombok.Data;

import java.util.List;

@Data
public class GetSelfContractRespVO {
    private List<GetContractInfoRespVO> list;
}
