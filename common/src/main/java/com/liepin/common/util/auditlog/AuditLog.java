package com.liepin.common.util.auditlog;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.liepin.common.config.thread.AsyncExecutor;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.auditlog.constant.TableName;
import com.liepin.common.util.auditlog.entity.Audit;
import com.liepin.common.util.auditlog.mapper.AuditMapper;
import com.liepin.common.util.time.TimeUtil;

public class AuditLog {
    public static void setAuditPass(final TableName tableName,final Long tableId){
        AsyncExecutor.getExecutor().execute(new Thread(() -> {
            AuditMapper auditMapper = SpringUtil.getBean(AuditMapper.class);
            // 组装参数
            Audit newAudit = new Audit();
            newAudit.setTableName(tableName.getTableName());
            newAudit.setTableId(tableId);
            newAudit.setAuditTime(TimeUtil.getNowWithMin());
            newAudit.setAuditId(StpUtil.getLoginIdAsLong());
            newAudit.setAuditStatus(ConstantsEnums.AuditStatus.PASS.getStatus());
            auditMapper.insert(newAudit);
        }));
    }

    public static void setAuditFail(final TableName tableName,final Long tableId){
        AsyncExecutor.getExecutor().execute(new Thread(() -> {
            AuditMapper auditMapper = SpringUtil.getBean(AuditMapper.class);
            // 组装参数
            Audit newAudit = new Audit();
            newAudit.setTableName(tableName.getTableName());
            newAudit.setTableId(tableId);
            newAudit.setAuditTime(TimeUtil.getNowWithMin());
            newAudit.setAuditId(StpUtil.getLoginIdAsLong());
            newAudit.setAuditStatus(ConstantsEnums.AuditStatus.FAIL.getStatus());
            auditMapper.insert(newAudit);
        }));
    }
}
