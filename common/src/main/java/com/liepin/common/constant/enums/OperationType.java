package com.liepin.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OperationType {
    @Getter
    @AllArgsConstructor
    class INSERT implements OperationType {
        public static final String value = "插入类型";
    }
    @Getter
    @AllArgsConstructor
    class DELETE implements OperationType {
        public static final String value = "删除类型";
    }
    @Getter
    @AllArgsConstructor
    class UPDATE implements OperationType {
        public static final String value = "更新类型";
    }
    @Getter
    @AllArgsConstructor
    class SERACH implements OperationType {
        public static final String value = "查询类型";
    }

}
