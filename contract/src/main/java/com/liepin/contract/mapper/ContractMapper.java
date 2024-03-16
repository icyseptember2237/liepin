package com.liepin.contract.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {
    String selectEnterpriseNameByPrivateId(@Param("privateId") Long privateId);

    String selectTalentNameByPrivateId(@Param("privateId") Long privateId);

    boolean updateRequireStatusBatch(@Param("list")List<Long> ids, @Param("status") String status);

    boolean updateMatchStatusBatch(@Param("list")List<Long> ids, @Param("status") String status);
}
