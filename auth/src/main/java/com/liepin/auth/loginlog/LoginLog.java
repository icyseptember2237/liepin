package com.liepin.auth.loginlog;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liepin.auth.entity.vo.req.UserLoginReqVO;
import com.liepin.auth.loginlog.entity.SysLog;
import com.liepin.auth.loginlog.service.impl.SysLogServiceImpl;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.config.thread.AsyncExecutor;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
@Aspect
public class LoginLog {

    private static final String LOCAL_IP = "127.0.0.1";



    @AfterThrowing(pointcut = "@annotation(controllerLog)",throwing = "e")
    public void excption(JoinPoint joinPoint,AutoLog controllerLog,Exception e){
        UserLoginReqVO req = getReq(joinPoint);

        SysLog log = new SysLog();
        log.setIp((getIpAddr(((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest())));
        log.setUsername(req.getUsername());
        log.setRes(ConstantsEnums.YESNOWAIT.NO.getValue());
        log.setTime(TimeUtil.getNowWithSec());
        if (ExceptionsEnums.Login.USER_ERROR.getMsg().equals(e.getMessage()))
            log.setMsg("密码错误");
        else if (ExceptionsEnums.Login.USER_CLOSE.getMsg().equals(e.getMessage()))
            log.setMsg("账号已禁用");
        else if (ExceptionsEnums.Login.ACCOUNT_NOT_EXT.getMsg().equals(e.getMessage()))
            log.setMsg("账号不存在");
        else
            log.setMsg("其他原因");
        // 异步存入
        AsyncExecutor.getExecutor().schedule(new Thread(() ->{
            SpringUtil.getBean(SysLogServiceImpl.class).save(log);
        }));
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public static void writeLog(JoinPoint joinPoint,AutoLog controllerLog,Object jsonResult){
        // 参数转换
        UserLoginReqVO req = getReq(joinPoint);
        // 日志内容组装
        SysLog log = new SysLog();
        log.setUsername(req.getUsername());
        log.setRes(ConstantsEnums.YESNOWAIT.YES.getValue());
        log.setMsg("登录成功");
        log.setTime(TimeUtil.getNowWithSec());
        log.setIp(getIpAddr(((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()));
        // 异步存入
        AsyncExecutor.getExecutor().schedule(new Thread(() ->{
            SpringUtil.getBean(SysLogServiceImpl.class).save(log);
        }));
    }

    private static UserLoginReqVO getReq(JoinPoint joinPoint){
        try {
            Object[] args = joinPoint.getArgs();
            String jsonString = JSON.toJSONString(args);
            JSONArray array = JSONArray.parseArray(jsonString);
            JSONObject object = array.getJSONObject(0);
            return JSON.toJavaObject(object,UserLoginReqVO.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
    }


}
