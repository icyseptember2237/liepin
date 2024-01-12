package com.liepin.talent.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liepin.talent.entity.base.SendTo;
import com.liepin.talent.mapper.base.TalentSendToMapper;
import com.liepin.talent.service.base.TalentSendToService;
import org.springframework.stereotype.Service;

@Service
public class TalentSendToServiceImpl extends ServiceImpl<TalentSendToMapper, SendTo> implements TalentSendToService {
}
