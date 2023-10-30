package com.liepin.common.constant.classes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@Getter
@ApiModel(description = "通用消息返回类")
public final class Result <T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int SUCCESS = 200;
    private static final int FAIL = 500;


    @ApiModelProperty(value = "200",notes = "响应码")
    private int code;

    @ApiModelProperty(value = "操作成功",notes = "响应消息")
    private String msg;

    @ApiModelProperty(value = "[]",notes = "数据")
    private T data;

    public static <T> Result<T> success()
    {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data)
    {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Result<T> fail()
    {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> Result<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> Result<T> fail(T data)
    {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> Result<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> Result<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg)
    {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

}
