package com.liepin.contract.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liepin.common.constant.classes.Result;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LockAspect {

    private final RedisTemplate redisTemplate;

    @Autowired
    public LockAspect(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }



    @Pointcut(value = "@annotation(com.liepin.contract.aspect.LockContract)")
    public void lockContract() {

    }

    @Around(value = "lockContract()")
    public Object getLock(ProceedingJoinPoint point) throws Throwable{
        Long contractId = getContractIdFromReq(point);
        if (contractId != null){
            if (getLock(contractId)){
                return point.proceed();
            } else {
                return Result.fail();
            }
        }
        return Result.fail("缺少参数");
    }

    @AfterReturning(pointcut = "@annotation(lockContract)")
    public void afterReturning(JoinPoint point,LockContract lockContract){
        Long contractId = getContractIdFromReq(point);
        freeLock(contractId);
    }

    @AfterThrowing(pointcut = "@annotation(lockContract)")
    public void afterThrowing(JoinPoint point,LockContract lockContract){
        Long contractId = getContractIdFromReq(point);
        freeLock(contractId);
    }

    private Long getContractIdFromReq(JoinPoint point){
        String contractId;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            contractId = request.getParameter("contractId");
            if (StringUtils.isNotEmpty(contractId)){
                return Long.parseLong(contractId);
            }

            Object[] args = point.getArgs();
            String jsonString = JSON.toJSONString(args);
            JSONArray array = JSONArray.parseArray(jsonString);
            JSONObject object = array.getJSONObject(0);
            contractId = object.get("contractId").toString();
            if (StringUtils.isNotEmpty(contractId)){
                return Long.parseLong(contractId);
            }
        } catch (Throwable t){
            return null;
        }
        return null;
    }

    synchronized private boolean getLock(Long contractId){
        try {
            for (int i = 0;i < 20;i++){
                if (redisTemplate.opsForValue().setIfAbsent("Contract:lock:" + contractId,"lock",60, TimeUnit.SECONDS))
                    return true;
                else {
                    Thread.sleep(50);
                }
            }
        } catch (Exception e){
        }
        return false;
    }

    synchronized private void freeLock(Long contractId){
        redisTemplate.delete("Contract:lock:" + contractId);
    }
}
