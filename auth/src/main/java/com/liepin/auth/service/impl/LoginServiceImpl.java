package com.liepin.auth.service.impl;

import com.liepin.auth.entity.base.User;
import com.liepin.auth.entity.vo.UserLoginReqVO;
import com.liepin.auth.mapper.LoginMapper;
import com.liepin.auth.service.LoginService;
import com.liepin.auth.util.Crypto;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private static LoginMapper loginMapper;

    @Override
    public Result userLogin(UserLoginReqVO reqVO){
        // 数据检查
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getUsername()) && StringUtils.isNotEmpty(reqVO.getPassword())
                ,ExceptionsEnums.Login.INFO_EMPTY);

        // 获取账号信息
        User user = loginMapper.getUserByUsername(reqVO.getUsername());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user), ExceptionsEnums.Login.ACCOUNT_NOT_EXT);

        // 检查密码
        AssertUtils.isFalse(reqVO.getPassword().equals(Crypto.md5(reqVO.getPassword())),ExceptionsEnums.Login.USER_ERROR);

        // 检查账号状态
        AssertUtils.isFalse();

    }
}
