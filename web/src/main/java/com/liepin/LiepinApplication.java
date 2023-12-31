package com.liepin;

import cn.hutool.extra.spring.SpringUtil;
import com.liepin.common.config.reqloghandel.EnableServletRequestLog;
import com.liepin.common.constant.config.FileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
//@EnableServletRequestLog
public class LiepinApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiepinApplication.class, args);
        Environment environment = SpringUtil.getBean(Environment.class);
        log.info("启动成功，位于端口" + environment.getProperty("server.port"));
        char[] charArray = "s".toCharArray();
    }
}