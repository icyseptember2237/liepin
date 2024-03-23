package com.liepin.contract.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.contract.entity.base.Performance;
import com.liepin.contract.mapper.base.PerformanceMapper;
import com.liepin.contract.service.base.PerformanceService;
import org.springframework.stereotype.Service;

@Service
public class PerformanceServiceImpl extends ServiceImpl<PerformanceMapper, Performance> implements PerformanceService {
}
