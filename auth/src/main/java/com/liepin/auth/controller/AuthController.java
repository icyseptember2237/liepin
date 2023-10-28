package com.liepin.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.entity.base.Role;
import com.liepin.auth.entity.base.User;
import com.liepin.auth.entity.vo.req.CreateUserReqVO;
import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.req.UpdateUserInfoReqVO;
import com.liepin.auth.entity.vo.req.UpdateUserPasswordReqVO;
import com.liepin.auth.entity.vo.resp.GetUserInfoRespVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.auth.service.AuthService;
import com.liepin.common.constant.classes.HashResult;
import com.liepin.common.constant.classes.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "权限控制-账号管理")
@RequestMapping("/api/auth/manage")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/getUserList")
    @ApiOperation(value = "管理员获取用户信息(条件或全部)")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<GetUsersRespVO> getUsers(GetUsersReqVO reqVO){
        return authService.getUsers(reqVO);
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "用户、管理员-获取个人信息")
    @SaCheckLogin
    public Result<GetUserInfoRespVO> getUserInfo(){
        return authService.getUserInfo();
    }

    @GetMapping("/getAllRoles")
    @ApiModelProperty(value = "管理员-获取所有角色信息")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<List<Role>> getAllRoles(){
        return authService.getAllRoles();
    }

    @PostMapping("/createUser")
    @ApiOperation(value = "管理员-新建用户")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result createUser(CreateUserReqVO user){
        return authService.createUser(user);
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "管理员-更新用户信息")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result updateUserInfo(UpdateUserInfoReqVO reqVO){
        return authService.updateUserInfo(reqVO);
    }

    @PostMapping("/updateUserPassword")
    @ApiOperation(value = "用户-修改密码")
    @SaCheckLogin
    public Result updateUserPassWord(@RequestBody UpdateUserPasswordReqVO reqVO){
        return authService.updateUserPassWord(reqVO);
    }

    @GetMapping("/resetUserPassword")
    @ApiOperation(value = "管理员-重置用户密码为123456")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result resetPassword(@RequestParam Long id){
        return authService.resetPassword(id);
    }

    @GetMapping("/banUser")
    @ApiOperation(value = "管理员-禁用员工")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result banUser(@RequestParam Long id){
        return authService.banUser(id);
    }

    @GetMapping("/activateUser")
    @ApiOperation(value = "管理员-启用员工")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result activateUser(@RequestParam Long id){
        return authService.activateUser(id);
    }

    @GetMapping("/deleteUser")
    @ApiOperation(value = "管理员-删除员工")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result deleteUser(@RequestParam Long id){
        return authService.deleteUser(id);
    }



}
