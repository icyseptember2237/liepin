package com.liepin.enterprise.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.enterprise.entity.base.SendTo;
import com.liepin.enterprise.mapper.base.EnterpriseSendToMapper;
import com.liepin.enterprise.service.base.EnterpriseSendToService;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseSendToServiceImpl extends ServiceImpl<EnterpriseSendToMapper, SendTo> implements EnterpriseSendToService {
}
