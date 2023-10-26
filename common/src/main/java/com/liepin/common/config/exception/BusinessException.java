package com.liepin.common.config.exception;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;


public class BusinessException extends RuntimeException implements Exceptions{
    private static final long serialVersionUID = -6403033409061476727L;
    private Integer code;
    private final String msg;

    public BusinessException(String msg){
        super(msg);
        this.code = 500;
        this.msg = msg;
    }

    public BusinessException(int exceptionCode, String msg){
        super(msg);
        this.code = exceptionCode;
        this.msg = msg;
    }

    public BusinessException(ExceptionsEnums exception,String msg){
        super(msg);
        this.code = exception.getCode();
        this.msg = msg;
    }

    public BusinessException(ExceptionsEnums exception){
        this(exception, exception.getMsg());
    }

    public BusinessException(int exceptionCode, String msg, Throwable cause) {
        super(msg, cause);
        this.code = 500;
        this.code = exceptionCode;
        this.msg = msg;
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.code = 500;
        this.msg = msg;
    }

    public BusinessException(ExceptionsEnums exception, Object... args) {
        this(exception, ArrayUtils.isNotEmpty(args) ? String.format(exception.getMsg(), args) : exception.getMsg());
    }

    @Override
    public int getCode(){
        return this.code;
    }

    @Override
    public String getMsg(){
        return this.msg;
    }
}
