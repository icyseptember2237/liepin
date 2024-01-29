package com.liepin.contract.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.contract.entity.base.ContractMatch;
import com.liepin.contract.mapper.base.ContractMatchMapper;
import com.liepin.contract.service.base.ContractMatchService;
import org.springframework.stereotype.Service;

@Service
public class ContractMatchServiceImpl extends ServiceImpl<ContractMatchMapper, ContractMatch> implements ContractMatchService {
}
