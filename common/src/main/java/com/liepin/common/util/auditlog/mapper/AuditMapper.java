package com.liepin.common.util.auditlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.common.util.auditlog.entity.Audit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuditMapper extends BaseMapper<Audit> {
    Audit getByTableNameAndId(@Param("tableName") String tableName,@Param("tableId") Long tableId);
}
