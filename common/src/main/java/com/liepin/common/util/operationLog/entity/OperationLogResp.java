package com.liepin.common.util.operationLog.entity;

import lombok.Data;

import java.util.List;

@Data
public class OperationLogResp {
    private List<OperationLog> list;
    private Long total;
}
