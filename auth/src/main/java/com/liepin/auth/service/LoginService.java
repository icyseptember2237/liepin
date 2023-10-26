package com.liepin.auth.service;

import com.liepin.auth.entity.vo.UserLoginReqVO;
import com.liepin.common.constant.classes.Result;

public interface LoginService {

    Result userLogin(UserLoginReqVO reqVO);
}
