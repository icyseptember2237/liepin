package com.liepin.common.util.time;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

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

    public static String getLastWorkDay(){
        //1是假期
        Integer result = 1;
        LocalDate time = LocalDate.now();
        while (result == 1){
            time = time.minus(1, ChronoUnit.DAYS);
            RestTemplate restTemplate = new RestTemplate();
            String httpUrl = "http://tool.bitefu.net/jiari/?d=" + time;
            result = restTemplate.getForObject(httpUrl, Integer.class);

        }
        return String.valueOf(time);
    }
}
