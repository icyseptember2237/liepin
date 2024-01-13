package com.liepin.talent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalentMatchStatus {
    // 可匹配
    READY("READY"),
    // 匹配后待审核
    WAIT("WAIT"),
    // 审核通过
    PASS("PASS");
    private String status;

}
