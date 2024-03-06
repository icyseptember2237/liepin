package com.liepin.enterprise.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnterprisePrivateStatus {
    // 未联系
    NOT_CONTACT("NOT_CONTACT"),
    // 跟进中
    FOLLOWUP("FOLLOWUP"),
    // 已有合同需求
    CONTRACT("CONTRACT"),
    // 待匹配
    WAIT_MATCH("WAIT_MATCH"),
    // 匹配中
    MATCHING("MATCHING");   
    private String status;
}
