package com.liepin.common.config.reqloghandel;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Import({EnableServletRequestLogImportSelector.class})
public @interface EnableServletRequestLog {
}
