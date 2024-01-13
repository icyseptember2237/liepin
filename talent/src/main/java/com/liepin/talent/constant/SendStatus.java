package com.liepin.talent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 内推状态
public enum SendStatus {
    //未内推
    NO("NO"),
    // 内推审核中
    WAIT("WAIT");
    private String status;
}
