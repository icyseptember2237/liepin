package com.liepin.common.aspect.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    int times() default 10;
    long withinTime() default 60;
    long blockTime() default 120;
    TimeUnit timeunit() default TimeUnit.SECONDS;

}
