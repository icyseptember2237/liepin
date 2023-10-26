package com.liepin.auth.controller;

import com.liepin.auth.entity.vo.UserLoginReqVO;
import com.liepin.auth.service.LoginService;
import com.liepin.common.constant.classes.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class LoginController {

    @Autowired
    private static LoginService loginService;

    @PostMapping("/userLogin")
    public Result login(@RequestBody UserLoginReqVO reqVO){
        return loginService.userLogin(reqVO);
    }
}
