package com.liepin.common.aspect.operationAspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.liepin.common.config.thread.AsyncExecutor;
import com.liepin.common.constant.enums.OperationStatus;
import com.liepin.common.util.operationLog.entity.IpAddressUtil;
import com.liepin.common.util.operationLog.entity.OperationLog;
import com.liepin.common.util.operationLog.service.OperationLogService;
import com.liepin.common.util.operationLog.service.serviceImpl.OperationLogServiceImpl;
import com.liepin.common.util.time.TimeUtil;
import org.aopalliance.intercept.Joinpoint;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    private OperationLogService operationLogService;

    @Pointcut(value = "@annotation(com.liepin.common.aspect.operationAspect.OperationAspect)")
    public void operationLogPointCut(){
    }

    @AfterReturning(value = "operationLogPointCut()", returning = "data")
    public void saveOperationLog(JoinPoint joinPoint, Object data){
        //获取request请求，用于解析IP地址
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        OperationLog operationLog = new OperationLog();
        long id = StpUtil.getLoginIdAsLong();

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationAspect annotation = method.getAnnotation(OperationAspect.class);
            operationLog.setOperatorName(operationLogService.getOperatiorName(id));
            operationLog.setType(annotation.type());
            operationLog.setCreateTime(TimeUtil.getNowWithSec());
            operationLog.setModule(annotation.module());
            operationLog.setOperationIp(IpAddressUtil.getIpAddrAsIPv4(request));
            operationLog.setOperationDetail(annotation.detail());
            operationLog.setOperatorId(StpUtil.getLoginIdAsLong());
            operationLog.setOperationStatus(OperationStatus.SUCCESS.value);
//TODO 后续测试再开启插入日志，不然太多日志到数据库里了

//            operationLogService.insertOperationLog(operationLog);
//            AsyncExecutor.getExecutor().schedule(new Thread(() ->{
//                SpringUtil.getBean(OperationLogServiceImpl.class).insertOperationLog(operationLog);
//            }));
        }catch (Exception e){
            System.err.println(e);
        }
        return ;
    }
    @AfterThrowing(value = "operationLogPointCut()")
    public void saveOperationThrowLog(JoinPoint joinPoint){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        long id = StpUtil.getLoginIdAsLong();

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationAspect annotation = method.getAnnotation(OperationAspect.class);
            operationLog.setOperatorName(operationLogService.getOperatiorName(id));
            operationLog.setType(annotation.type());
            operationLog.setCreateTime(TimeUtil.getNowWithSec());
            operationLog.setModule(annotation.module());
            operationLog.setOperationIp(IpAddressUtil.getIpAddrAsIPv4(request));
            operationLog.setOperationDetail(annotation.detail());
            operationLog.setOperatorId(StpUtil.getLoginIdAsLong());
            operationLog.setOperationStatus(OperationStatus.FAIL.value);
//TODO 后续测试再开启插入日志，不然太多日志到数据库里了

//            AsyncExecutor.getExecutor().schedule(new Thread(() ->{
//                SpringUtil.getBean(OperationLogServiceImpl.class).insertOperationLog(operationLog);
//            }));
//            operationLogService.insertOperationLog(operationLog);
        }catch (Exception e){
            System.err.println(e);
        }
        return ;
    }

}
