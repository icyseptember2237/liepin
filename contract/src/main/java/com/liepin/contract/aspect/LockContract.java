package com.liepin.contract.aspect;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Order(1)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LockContract {
}
