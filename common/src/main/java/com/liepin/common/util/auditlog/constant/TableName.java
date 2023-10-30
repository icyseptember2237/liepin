package com.liepin.common.util.auditlog.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableName {
    AGENCY("agency");
    private String tableName;
}
