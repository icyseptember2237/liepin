package com.liepin.common.config.reqloghandel;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


public class ServletRequestLogConfiguration {
    public ServletRequestLogConfiguration() {
    }

    @Bean
    public FilterRegistrationBean webDefaultFilter() {
        FilterRegistrationBean<RequestLogFilter> registration = new FilterRegistrationBean(new RequestLogFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
