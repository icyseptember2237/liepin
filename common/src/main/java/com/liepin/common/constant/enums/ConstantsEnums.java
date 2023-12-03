package com.liepin.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ConstantsEnums {
    @Getter
    @AllArgsConstructor
    enum SystemType implements ConstantsEnums {
        WINDOWS(1),
        LINUX(2),
        OTHER(-1);
        private int value;
    }

    @Getter
    @AllArgsConstructor
    enum YESNOWAIT implements ConstantsEnums {
        YES("YES"),
        NO("NO"),
        WAIT("WAIT");
        private String value;
    }

    @Getter
    @AllArgsConstructor
    enum AuditStatus implements ConstantsEnums {
        PASS("PASS"),
        WAIT("WAIT"),
        FAIL("FAIL");
        private String status;
    }
}
