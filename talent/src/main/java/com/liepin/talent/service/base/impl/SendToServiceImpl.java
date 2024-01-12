package com.liepin.talent.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liepin.talent.entity.base.SendTo;
import com.liepin.talent.mapper.base.SendToMapper;
import com.liepin.talent.service.base.SendToService;
import org.springframework.stereotype.Service;

@Service
public class SendToServiceImpl extends ServiceImpl<SendToMapper, SendTo> implements SendToService {
}
