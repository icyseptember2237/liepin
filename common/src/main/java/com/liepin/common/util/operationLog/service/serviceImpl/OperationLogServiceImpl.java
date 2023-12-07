package com.liepin.common.util.operationLog.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.operationLog.entity.OperationLog;
import com.liepin.common.util.operationLog.entity.OperationLogResp;
import com.liepin.common.util.operationLog.mapper.OperationLogMapper;
import com.liepin.common.util.operationLog.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public Result<OperationLogResp> getOperationLog() {
        OperationLogResp logResp = new OperationLogResp();

        List<OperationLog> operationLogList = operationLogMapper.selectList(new LambdaQueryWrapper<>());

        logResp.setList(operationLogList);

        logResp.setTotal(operationLogMapper.selectCount(new LambdaQueryWrapper<>()));

        return Result.success(logResp);
    }

    @Override
    public void insertOperationLog(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
        return;
    }

    @Override
    public String getOperatiorName(long id) {
        String name = operationLogMapper.getOperatorName(id);
        return name;
    }
}
