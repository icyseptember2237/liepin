package com.liepin.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OperationStatus {
    @Getter
    @AllArgsConstructor
    class SUCCESS implements OperationStatus {
        public static final String value = "操作成功";
    }
    @Getter
    @AllArgsConstructor
    class FAIL implements OperationStatus {
        public static final String value = "操作失败";
    }
}
