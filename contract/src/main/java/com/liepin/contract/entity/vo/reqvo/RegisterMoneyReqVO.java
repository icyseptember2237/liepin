package com.liepin.contract.entity.vo.reqvo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegisterMoneyReqVO {
    private Long contractId;
    private BigDecimal money;
}
