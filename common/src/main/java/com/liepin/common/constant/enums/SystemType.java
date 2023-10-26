package com.liepin.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemType {
    WINDOWS(1),
    LINUX(2),
    OTHER(-1);
    private int value;
}
