package com.liepin.auth.service;

import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.common.constant.classes.HashResult;

public interface AuthService {
    HashResult<GetUsersRespVO> getUsers(GetUsersReqVO reqVO);
}
