package com.liepin.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OperationModule {
    @Getter
    @AllArgsConstructor
    class WORKLOG implements OperationModule {
        public static final String value = "工作日志模块";
    }

    @Getter
    @AllArgsConstructor
    class AGENCY implements OperationModule {
        public static final String value = "中介模块";
    }
    @Getter
    @AllArgsConstructor
    class LOGIN implements OperationModule {
        public static final String value = "登录模块";
    }
    @Getter
    @AllArgsConstructor
    class AUTH implements OperationModule {
        public static final String value = "权限模块";
    }
    @Getter
    @AllArgsConstructor
    class ENTERPRISE implements OperationModule {
        public static final String value = "企业模块";
    }
    @Getter
    @AllArgsConstructor
    class TALENT implements OperationModule {
        public static final String value = "人才模块";
    }

}
