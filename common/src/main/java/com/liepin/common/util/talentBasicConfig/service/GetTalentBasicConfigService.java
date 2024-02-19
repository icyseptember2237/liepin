package com.liepin.common.util.talentBasicConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;

public interface GetTalentBasicConfigService {
    Result<GetTalentBasicConfigResVO> getTalentBasicConfig();
}
