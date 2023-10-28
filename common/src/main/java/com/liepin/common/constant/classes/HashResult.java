package com.liepin.common.constant.classes;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Objects;

public class HashResult<T> extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;
    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String DATA_TAG = "data";

    public enum Type
    {
        SUCCESS(200),
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

    }

    public HashResult()
    {
    }

    public HashResult(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    public HashResult(Type type, String msg, T data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (ObjectUtil.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }



    public static HashResult success()
    {
        return HashResult.success("操作成功");
    }

    public static <T> HashResult<T> success(T data)
    {
        return HashResult.success("操作成功", data);
    }

    public static HashResult success(String msg)
    {
        return HashResult.success(msg, null);
    }

    public static <T> HashResult<T> success(String msg, T data)
    {
        return new HashResult(Type.SUCCESS, msg, data);
    }



    public static HashResult error()
    {
        return HashResult.error("操作失败");
    }

    public static HashResult error(String msg)
    {
        return HashResult.error(msg, null);
    }

    public static <T> HashResult<T> error(String msg, T data)
    {
        return new HashResult(Type.ERROR, msg, data);
    }

    public boolean isSuccess()
    {
        return Objects.equals(Type.SUCCESS.value, this.get(CODE_TAG));
    }


    public boolean isError()
    {
        return Objects.equals(Type.ERROR.value, this.get(CODE_TAG));
    }

    @Override
    public HashResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
