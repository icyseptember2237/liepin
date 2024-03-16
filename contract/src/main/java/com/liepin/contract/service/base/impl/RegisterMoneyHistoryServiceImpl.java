package com.liepin.contract.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.contract.entity.base.RegisterMoneyHistory;
import com.liepin.contract.mapper.base.RegisterMoneyHistoryMapper;
import com.liepin.contract.service.base.RegisterMoneyHistoryService;
import org.springframework.stereotype.Service;

@Service
public class RegisterMoneyHistoryServiceImpl extends ServiceImpl<RegisterMoneyHistoryMapper, RegisterMoneyHistory> implements RegisterMoneyHistoryService {
}
