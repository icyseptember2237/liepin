package com.liepin.common.config.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

public class AssertUtils {

    //随时随地，想抛就抛!
    public static void throwException(ExceptionsEnums exception) throws BusinessException{
        throw new BusinessException(exception);
    }

    public static void throwException(int code, String msg) throws BusinessException{
        throw new BusinessException(code,msg);
    }

    public static void throwException(String msg) throws BusinessException{
        throw new BusinessException(msg);
    }

    public static void isFalse(boolean expression,ExceptionsEnums exception) throws BusinessException{
        if (!expression){
            throw new BusinessException(exception);
        }
    }
    public static void isFalse(boolean expression,String msg) throws BusinessException{
        if (!expression){
            throw new BusinessException(msg);
        }
    }

    public static void isBlank(String string, ExceptionsEnums exception) throws BusinessException {
        if (StrUtil.isBlank(string)) {
            throw new BusinessException(exception);
        }
    }

    public static void isNull(Object object,ExceptionsEnums exception) throws BusinessException{
        if (object == null){
            throw new BusinessException(exception);
        }
    }

    public static void isEmpty(Object[] array,ExceptionsEnums exception) throws BusinessException{
        if (ObjectUtils.isEmpty(array)){
            throw new BusinessException(exception);
        }
    }

    public static <T> void isEmpty(List<T> list,ExceptionsEnums exception) throws BusinessException{
        if (list.isEmpty()){
            throw new BusinessException(exception);
        }
    }

    public static void isEmpty(Collection<?> collection, ExceptionsEnums exception) throws BusinessException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(exception);
        }
    }
}
