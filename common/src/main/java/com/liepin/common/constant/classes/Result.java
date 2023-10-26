package com.liepin.common.constant.classes;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Objects;

public class Result extends HashMap<String,Object> {
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

    public Result()
    {
    }

    public Result(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    public Result(Type type, String msg, Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (ObjectUtil.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }



    public static Result success()
    {
        return Result.success("操作成功");
    }

    public static Result success(Object data)
    {
        return Result.success("操作成功", data);
    }

    public static Result success(String msg)
    {
        return Result.success(msg, null);
    }

    public static Result success(String msg, Object data)
    {
        return new Result(Type.SUCCESS, msg, data);
    }



    public static Result error()
    {
        return Result.error("操作失败");
    }

    public static Result error(String msg)
    {
        return Result.error(msg, null);
    }

    public static Result error(String msg, Object data)
    {
        return new Result(Type.ERROR, msg, data);
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
    public Result put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
