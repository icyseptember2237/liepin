package com.liepin.contract.entity.vo.reqvo;

import lombok.Data;

import java.util.List;

@Data
public class CancelMatchReqVO {
    private Long contractId;
    private List<Long> talentIds;
}
