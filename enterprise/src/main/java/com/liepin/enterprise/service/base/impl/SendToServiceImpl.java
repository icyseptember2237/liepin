package com.liepin.enterprise.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.enterprise.entity.base.SendTo;
import com.liepin.enterprise.mapper.base.SendToMapper;
import com.liepin.enterprise.service.base.SendToService;
import org.springframework.stereotype.Service;

@Service
public class SendToServiceImpl extends ServiceImpl<SendToMapper, SendTo> implements SendToService {
}
