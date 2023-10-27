package com.liepin.common.util.time;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtil {

    private static final DateTimeFormatter day =DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter timeWithMil = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter timeWithMin = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getToday(){
        return LocalDateTime.now().format(day);
    }

    public static String getNowWithMin(){
        return LocalDateTime.now().format(timeWithMin);
    }

    public static String getNowWithSec(){
        return LocalDateTime.now().format(time);
    }

    public static String getNowWithMilSec(){
        return LocalDateTime.now().format(timeWithMil);
    }
}
