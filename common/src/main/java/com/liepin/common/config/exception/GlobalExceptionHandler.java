package com.liepin.common.config.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    static final String ERROR = "业务异常";

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception ex, HttpServletResponse response, HttpServletRequest request) throws Throwable{
        int errorCode = 500;
        String errorMsg;
        if (ex instanceof Exceptions) {     //AssertUtils断言抛出的自定义业务异常
            errorCode = ((Exceptions) ex).getCode();
            errorMsg = ((Exceptions) ex).getMsg();
            if (log.isDebugEnabled()){
                log.debug(ERROR, ex);
            }
        } else if (ex instanceof NotLoginException){
            errorCode = Integer.parseInt(((NotLoginException) ex).getType());
            errorMsg = ex.getMessage();
            if (log.isDebugEnabled()){
                log.debug(ERROR, ex);
            }
        } else if (ex instanceof SaTokenException){    //处理sa-token相关异常
            errorCode = ((SaTokenException) ex).getCode();
            errorMsg = ex.getMessage();
            if (log.isDebugEnabled()){
                log.debug(ERROR,ex);
            }
        } else if (ex instanceof MyBatisSystemException) {
            errorCode = 500;
            errorMsg = ex.getMessage();
            if (log.isDebugEnabled()){
                log.debug(ERROR, ex);
            }
        } else if (ex instanceof MethodArgumentNotValidException) {
            //reqvo参数校验错误处理
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = exs.getBindingResult();
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage()).append("。");
            }
            errorCode = 010101;
            errorMsg = sb.toString();
            if (log.isDebugEnabled()){
                log.debug(ERROR, ex);
            }
        } else {    //未处理错误
            errorMsg = ex.getMessage() == null ? "系统繁忙,请稍后再试..." : ex.getMessage();
            if (log.isInfoEnabled()) {
                log.info(ex.getMessage() == null ? "unprocessed exception." : ERROR, ex);
            }
            response.setStatus(errorCode);
        }
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Content-Type", "application/json;charset=utf-8");
        try {
            String resultJson = "{\"code\": " + errorCode + ",\"msg\": \"" + errorMsg + "\",\"data\": null}";
            response.getWriter().write(resultJson);
            response.flushBuffer();
        } catch (IOException e) {
            log.warn("Write response failed.", e);
        }
    }
}
