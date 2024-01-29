package com.liepin.contract.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractMapper {
    String selectEnterpriseNameByPrivateId(@Param("privateId") Long privateId);

    String selectTalentNameByPrivateId(@Param("privateId") Long privateId);
}
