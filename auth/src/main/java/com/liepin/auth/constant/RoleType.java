package com.liepin.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


public interface RoleType {
    @Getter
    @AllArgsConstructor
    class MANAGER implements RoleType{
        public static final String code = "MANAGER";
        public static final String name = "总经办";

    }

    @Getter
    @AllArgsConstructor
    class TALENT implements RoleType{
        public static final String code = "TALENT";
        public static final String name = "人才部";
    }

    @Getter
    @AllArgsConstructor
    class ENTERPRISE implements RoleType{
        public static final String code = "ENTERPRISE";
        public static final String name = "单位部";
    }

}
