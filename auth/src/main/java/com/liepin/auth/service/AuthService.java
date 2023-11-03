package com.liepin.auth.service;

import com.liepin.auth.entity.base.Role;
import com.liepin.auth.entity.vo.req.*;
import com.liepin.auth.entity.vo.resp.GetLoginHistoryRespVO;
import com.liepin.auth.entity.vo.resp.GetUserInfoRespVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.common.constant.classes.Result;


import java.util.List;

public interface AuthService {
    Result<GetUsersRespVO> getUsers(GetUsersReqVO reqVO);

    Result<GetUserInfoRespVO> getUserInfo();

    Result createUser(CreateUserReqVO reqVO);

    Result<List<Role>> getAllRoles();

    Result<GetLoginHistoryRespVO> getLoginHistory(GetLoginHistoryReqVO reqVO);

    Result updateUserInfo(UpdateUserInfoReqVO reqVO);

    Result updateUserPassWord(UpdateUserPasswordReqVO reqVO);

    Result resetPassword(Long id);

    Result banUser(Long id);

    Result activateUser(Long id);

    Result deleteUser(Long id);
}
