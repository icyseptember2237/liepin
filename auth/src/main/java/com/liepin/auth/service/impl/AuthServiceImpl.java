package com.liepin.auth.service.impl;

import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.auth.mapper.AuthMapper;
import com.liepin.auth.service.AuthService;
import com.liepin.auth.service.base.RoleService;
import com.liepin.auth.service.base.UserService;
import com.liepin.common.constant.classes.HashResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthMapper authMapper;

    @Autowired
    public AuthServiceImpl(UserService userService,RoleService roleService,AuthMapper authMapper){
        this.authMapper = authMapper;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public HashResult<GetUsersRespVO> getUsers(GetUsersReqVO reqVO){
        GetUsersRespVO respVO = new GetUsersRespVO();
        respVO.setList(authMapper.getUsers(reqVO));
        respVO.setTotal(authMapper.getUsersNum(reqVO));
        return HashResult.success(respVO);
    }
}
