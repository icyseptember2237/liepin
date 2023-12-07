package com.liepin.common.util.operationLog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.operationLog.entity.OperationLog;
import com.liepin.common.util.operationLog.entity.OperationLogResp;

public interface OperationLogService extends IService<OperationLog> {
    Result<OperationLogResp> getOperationLog();

    void insertOperationLog(OperationLog operationLog);

    String getOperatiorName(long id);
}
