package com.liepin.common.util.talentBasicConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.talentBasicConfig.entity.Node;

import java.util.HashMap;
import java.util.List;

public interface GetTalentBasicConfigService {
    Result<Node> getTalentBasicConfig();
}
