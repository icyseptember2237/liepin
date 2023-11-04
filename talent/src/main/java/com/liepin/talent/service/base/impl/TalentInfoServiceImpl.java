package com.liepin.talent.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.talent.entity.base.TalentInfo;
import com.liepin.talent.mapper.base.TalentInfoMapper;
import com.liepin.talent.service.base.TalentInfoService;
import org.springframework.stereotype.Service;

@Service
public class TalentInfoServiceImpl extends ServiceImpl<TalentInfoMapper, TalentInfo> implements TalentInfoService {
}
