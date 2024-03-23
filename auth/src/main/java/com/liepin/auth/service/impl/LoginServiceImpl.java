package com.liepin.auth.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.entity.dto.LoginUser;
import com.liepin.auth.entity.vo.req.UserLoginReqVO;
import com.liepin.auth.entity.vo.resp.UserLoginRespVO;
import com.liepin.auth.mapper.LoginMapper;
import com.liepin.auth.service.LoginService;
import com.liepin.auth.util.Crypto;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final LoginMapper loginMapper;

    private final RedisTemplate redisTemplate;

    @Autowired
    public LoginServiceImpl(LoginMapper loginMapper,RedisTemplate redisTemplate){
        this.loginMapper = loginMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result<UserLoginRespVO> userLogin(UserLoginReqVO reqVO){
        // 数据检查
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getUsername()) && StringUtils.isNotEmpty(reqVO.getPassword())
                ,ExceptionsEnums.Login.INFO_EMPTY);

        // 获取账号信息
        LoginUser user = loginMapper.getUserByUsername(reqVO.getUsername());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user), ExceptionsEnums.Login.ACCOUNT_NOT_EXT);

        // 检查密码
        AssertUtils.isFalse(user.getPassword().equals(Crypto.md5(reqVO.getPassword())),ExceptionsEnums.Login.USER_ERROR);

        // 检查账号状态
        AssertUtils.isFalse(ConstantsEnums.YESNOWAIT.YES.getValue().equals(user.getStatus()),ExceptionsEnums.Login.USER_CLOSE);

        StpUtil.login(user.getId());
        StpUtil.getRoleList();

        UserLoginRespVO respVO = new UserLoginRespVO();
        respVO.setAuthorization(StpUtil.getTokenValue());
        respVO.setId(StpUtil.getLoginIdAsLong());
        respVO.setName(user.getName());
        respVO.setUsername(user.getUsername());
        respVO.setRole(user.getRoleCode());
        respVO.setCommitWorkLog(RoleType.MANAGER.code.equals(user.getRoleCode()) || checkWorkLog());

        return Result.success(respVO);
    }

    private boolean checkWorkLog(){
        return loginMapper.getCommitLog(StpUtil.getLoginIdAsLong())
                .stream()
                .anyMatch(log -> log.getCreateTime().contains(getLastWorkDay()));
    }

    private synchronized String getLastWorkDay(){
        Object res = redisTemplate.opsForValue().get("lastWorkDay.login");
        if (ObjectUtils.isNotEmpty(res))
            return String.valueOf(res);
        String day = TimeUtil.getLastWorkDayAsDay();
        redisTemplate.opsForValue().set("lastWorkDay.login",day,5, TimeUnit.HOURS);
        return day;
    }

    @Override
    @SaCheckLogin
    public Result userLogout(){
        StpUtil.logout(StpUtil.getLoginId());
        return Result.success();
    }
}
