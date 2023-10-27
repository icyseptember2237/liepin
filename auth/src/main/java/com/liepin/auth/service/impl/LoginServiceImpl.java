package com.liepin.auth.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.liepin.auth.entity.dto.LoginUser;
import com.liepin.auth.entity.vo.req.UserLoginReqVO;
import com.liepin.auth.mapper.LoginMapper;
import com.liepin.auth.service.LoginService;
import com.liepin.auth.util.Crypto;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.EnumsConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public Result userLogin(UserLoginReqVO reqVO){
        // 数据检查
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getUsername()) && StringUtils.isNotEmpty(reqVO.getPassword())
                ,ExceptionsEnums.Login.INFO_EMPTY);

        // 获取账号信息
        LoginUser user = loginMapper.getUserByUsername(reqVO.getUsername());
        log.info(user.getUsername());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user), ExceptionsEnums.Login.ACCOUNT_NOT_EXT);

        // 检查密码
        AssertUtils.isFalse(user.getPassword().equals(Crypto.md5(reqVO.getPassword())),ExceptionsEnums.Login.USER_ERROR);

        // 检查账号状态
        AssertUtils.isFalse(EnumsConstants.YESNO.YES.getValue().equals(user.getStatus()),ExceptionsEnums.Login.USER_CLOSE);

        StpUtil.login(user.getId());
        StpUtil.getRoleList();

        Result result = Result.success();
        HashMap<String,Object> data = new HashMap<>();
        data.put("Authorization",StpUtil.getTokenValue());
        data.put("id",StpUtil.getLoginId());
        data.put("role",user.getRoleName());
        data.put("name",user.getName());
        data.put("roleList",StpUtil.getLoginType());
        result.put("data",data);
        return result;

    }

    @Override
    @SaCheckLogin
    public Result userLogout(){
        log.info(StpUtil.getTokenValue());
        try {
            log.info(StpUtil.getLoginId().toString());
            StpUtil.logout(StpUtil.getLoginId());
        }catch (Exception e){
            e.printStackTrace();
            AssertUtils.throwException(ExceptionsEnums.Login.LOGOUT_FAIL);
        }
        return Result.success();
    }
}
