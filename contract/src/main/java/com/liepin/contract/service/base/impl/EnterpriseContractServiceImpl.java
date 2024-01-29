package com.liepin.contract.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.contract.entity.base.EnterpriseContract;
import com.liepin.contract.mapper.base.EnterpriseContractMapper;
import com.liepin.contract.service.base.EnterpriseContractService;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseContractServiceImpl extends ServiceImpl<EnterpriseContractMapper, EnterpriseContract> implements EnterpriseContractService {
}
