package com.liepin.contract.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.contract.entity.base.ContractAuditHistory;
import com.liepin.contract.mapper.base.ContractAuditHistoryMapper;
import com.liepin.contract.service.base.ContractAuditHistoryService;
import org.springframework.stereotype.Service;

@Service
public class ContractAuditHistoryServiceImpl extends ServiceImpl<ContractAuditHistoryMapper, ContractAuditHistory> implements ContractAuditHistoryService {
}
