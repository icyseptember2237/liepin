package com.liepin.talent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalentPrivateStatus {
    // 未联系
    NOT_CONTACT("NOT_CONTACT"),
    // 跟进中
    FOLLOWUP("FOLLOWUP"),
    // 匹配中
    MATCHING("MATCHING");
    private String status;
}
