package com.liepin.talent.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.talent.entity.base.TalentMatch;
import com.liepin.talent.mapper.base.TalentMatchMapper;
import com.liepin.talent.service.base.TalentMatchService;
import org.springframework.stereotype.Service;

@Service
public class TalentMatchServiceImpl extends ServiceImpl<TalentMatchMapper, TalentMatch> implements TalentMatchService {
}
