package com.liepin.auth.service;

import com.liepin.auth.entity.vo.req.UserLoginReqVO;
import com.liepin.auth.entity.vo.resp.UserLoginRespVO;
import com.liepin.common.constant.classes.HashResult;
import com.liepin.common.constant.classes.Result;

public interface LoginService {

    Result<UserLoginRespVO> userLogin(UserLoginReqVO reqVO);

    Result userLogout();
}
