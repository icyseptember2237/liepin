package com.liepin.common.util.operationLog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.common.util.operationLog.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    String getOperatorName(long id);

    List<OperationLog> getOperationList();

    void insertOperationLog(OperationLog operationLog);
}
