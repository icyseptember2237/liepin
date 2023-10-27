package com.liepin.auth.service;

import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.common.constant.classes.Result;

public interface AuthService {
    Result<GetUsersRespVO> getUsers(GetUsersReqVO reqVO);
}
