package com.liepin.common.aspect.ratelimit;

import cn.dev33.satoken.stp.StpUtil;
import com.liepin.common.constant.classes.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Order(0)
public class RateLimitAspect {

    private static final String KEY_PREFIX = "Count";
    private static final String OVER_RATE_PREFIX = "OverRateCount";
    private final RedisTemplate redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    @Autowired
    public RateLimitAspect(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Pointcut(value = "@annotation(com.liepin.common.aspect.ratelimit.RateLimit)")
    public void limit(){
    }

    @Around(value = "limit()")
    public Object rateCheck(ProceedingJoinPoint point) throws Throwable{
        MethodSignature signature = (MethodSignature) point.getSignature();
        System.out.println(signature.getMethod());
        RateLimit annotation = signature.getMethod().getAnnotation(RateLimit.class);
        int times = annotation.times();
        long withinTime = annotation.withinTime();
        long loginId = StpUtil.getLoginIdAsLong();
        long blockTime = annotation.blockTime();
        boolean byId = annotation.byId();
        TimeUnit timeUnit = annotation.timeunit();
        String key = signature.getMethod().getName() + KEY_PREFIX + loginId;
        String key1 = signature.getMethod().getName() + OVER_RATE_PREFIX + loginId;
        if (!byId){
            key = signature.getMethod().getName() + KEY_PREFIX;
            key1 = signature.getMethod().getName() + OVER_RATE_PREFIX;
        }

        // 每次访问设置次数+1
        redisTemplate.opsForValue().setIfAbsent(key,0);
        Long count = redisTemplate.opsForValue().increment(key,1);
        if (count == 1){
            redisTemplate.expire(key,withinTime,timeUnit);
        }

        Integer ban = (Integer) redisTemplate.opsForValue().get(key1);
        if (ban != null && ban >= 10 && byId){
            redisTemplate.expire(key1,30,TimeUnit.MINUTES);
            return Result.fail("操作过于频繁, 请多等几分钟后重试!");
        }

        // 超过限制禁止访问
        if (count != null && count >= times){
            // 一天内每次超过限制都将次数+1, 次数超过10次则封禁接口访问半小时
            redisTemplate.opsForValue().setIfAbsent(key1,0);
            Long overRateCount = redisTemplate.opsForValue().increment(key1,1);
            if (overRateCount == 1){
                redisTemplate.expire(key1,1,TimeUnit.DAYS);
            }
            // 超限10次封禁
            if (overRateCount != null && overRateCount >= 10){
                redisTemplate.expire(key1,blockTime,TimeUnit.SECONDS);
            }

            // 禁止期间再次访问重置时间
            redisTemplate.expire(key,withinTime,timeUnit);
            return Result.fail("操作频繁, 请稍后重试");
        }

        return point.proceed();
    }
}
