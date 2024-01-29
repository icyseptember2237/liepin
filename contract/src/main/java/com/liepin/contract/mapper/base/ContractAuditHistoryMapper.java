package com.liepin.contract.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.contract.entity.base.ContractAuditHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractAuditHistoryMapper extends BaseMapper<ContractAuditHistory> {
}
