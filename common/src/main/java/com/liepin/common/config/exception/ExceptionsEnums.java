package com.liepin.common.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ExceptionsEnums extends Exceptions{

    @Getter
    @AllArgsConstructor
    enum Common implements ExceptionsEnums {
        DATA_IS_ERROR(500, "数据错误!"),
        PARAMTER_IS_ERROR(500, "参数不正确！"),
        NO_RESOURCE(500, "请求资源不存在！"),
        DATETIME_BEFORE(500, "开始日期不能小于当前日期！");
        private int code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    enum UserEX implements ExceptionsEnums {
        USER_HAVE(500, "该账号或手机号已存在"),
        NOT_PREMISS(401, "无访问权限"),
        ACCOUNT_NOT_FIND(500, "未查询到该用户信息"),
        NO_LOGIN(500, "请求异常，无登录信息"),
        ROLE_NAME_REPEAT(500, "角色名称/编码重复,请重新输入");
        private int code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    enum Login implements ExceptionsEnums{
        INFO_EMPTY(500,"账号密码不能为空"),
        USER_ERROR(500, "账号或密码错误"),
        ACCOUNT_NOT_EXT(500, "登录账号不存在"),
        USER_CLOSE(500, "当前账号已被禁用,请联系管理员"),
        LOGOUT_FAIL(500,"登出失败"),
        NO_ROLE(500,"用户未分配角色");
        private int code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    enum Excel implements ExceptionsEnums{
        TEMPLATE_ERROR(500,"模板表头错误"),
        ERROR_READING_FILE_HEADER(500, "读取文件表头报错！");
        private int code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    enum File implements ExceptionsEnums{
        DOWNLOAD_FAIL(500,"文件下载失败"),
        ILLEGAL_FILENAME(500,"文件名称错误"),
        UPLOAD_FAIL(500,"文件上传失败"),
        NO_PATH_FOUND(500,"找不到服务器路径"),
        TYPE_NOT_ALLOWED(500,"非法文件类型");
        private int code;
        private String msg;
    }
    @Getter
    @AllArgsConstructor
    enum WorkLog implements ExceptionsEnums{
        WORK_LOG_EMPTY(500,"未上传日志");
        private int code;
        private String msg;
    }
}
