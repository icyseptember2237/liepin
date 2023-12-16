package com.liepin.common.aspect.operationAspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationAspect {
    String detail() default "";
    String module() default "";
    String type() default "";
}
