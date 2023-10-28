package com.liepin.auth.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.entity.base.User;
import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.auth.service.AuthService;
import com.liepin.common.constant.classes.HashResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "权限控制-账号管理")
@RequestMapping("/auth/manage")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/getUsers")
    @ApiOperation(value = "获取用户信息(条件或全部)")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public HashResult<GetUsersRespVO> getUsers(GetUsersReqVO reqVO){
        return authService.getUsers(reqVO);
    }

    @PostMapping("/createUser")
    @ApiOperation(value = "新建用户")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public HashResult createUser(User user){
        return null;
    }


}
