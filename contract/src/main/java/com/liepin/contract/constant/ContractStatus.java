package com.liepin.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractStatus {
    READY("READY"),
    MATCHING("MATCHING"),
    WAIT("WAIT"),
    PASS("PASS"),
    FAIL("FAIL");
    private String status;

}
