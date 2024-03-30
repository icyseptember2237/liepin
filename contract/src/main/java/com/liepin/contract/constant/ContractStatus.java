package com.liepin.contract.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractStatus {
    // 可匹配
    READY("READY"),
    // 匹配中
    MATCHING("MATCHING"),
    // 待审核
    WAIT("WAIT"),
    // 审核通过
    PASS("PASS"),
    // 不通过
    FAIL("FAIL"),
    // 未完结
    UNFINISH("UNFINISH"),
    // 已完结
    FINISH("FINISH");
    private String status;

}
