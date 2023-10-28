package com.liepin.auth.service;

import com.liepin.auth.entity.base.Role;
import com.liepin.auth.entity.vo.req.CreateUserReqVO;
import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.req.UpdateUserInfoReqVO;
import com.liepin.auth.entity.vo.req.UpdateUserPasswordReqVO;
import com.liepin.auth.entity.vo.resp.GetUserInfoRespVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.common.constant.classes.HashResult;
import com.liepin.common.constant.classes.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AuthService {
    Result<GetUsersRespVO> getUsers(GetUsersReqVO reqVO);

    Result<GetUserInfoRespVO> getUserInfo();

    Result createUser(CreateUserReqVO reqVO);

    Result<List<Role>> getAllRoles();

    Result updateUserInfo(UpdateUserInfoReqVO reqVO);

    Result updateUserPassWord(UpdateUserPasswordReqVO reqVO);

    Result resetPassword(Long id);

    Result banUser(Long id);

    Result activateUser(Long id);

    Result deleteUser(Long id);
}
