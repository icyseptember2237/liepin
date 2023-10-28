package com.liepin.auth.controller;

import com.liepin.auth.entity.vo.req.UserLoginReqVO;
import com.liepin.auth.service.LoginService;
import com.liepin.common.constant.classes.HashResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
@Api(value = "权限控制-登录")
public class LoginController {


    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/userLogin")
    @ApiOperation(value = "用户登录")
    public HashResult login(@RequestBody UserLoginReqVO reqVO){
        return loginService.userLogin(reqVO);
    }

    @PostMapping("/userLogout")
    @ApiOperation(value = "用户登出")
    public HashResult logout(){
        return loginService.userLogout();
    }
}
