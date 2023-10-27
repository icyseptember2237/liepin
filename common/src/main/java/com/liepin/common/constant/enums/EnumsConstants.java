package com.liepin.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface EnumsConstants {
    @Getter
    @AllArgsConstructor
    enum SystemType implements EnumsConstants{
        WINDOWS(1),
        LINUX(2),
        OTHER(-1);
        private int value;
    }

    @Getter
    @AllArgsConstructor
    enum YESNO implements EnumsConstants{
        YES("YES"),
        NO("NO");
        private String value;
    }
}
