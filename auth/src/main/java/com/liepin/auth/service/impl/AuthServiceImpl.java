package com.liepin.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liepin.auth.constant.RoleType;
import com.liepin.auth.entity.base.Role;
import com.liepin.auth.entity.base.User;
import com.liepin.auth.entity.vo.req.*;
import com.liepin.auth.entity.vo.resp.GetColleaguesRespVO;
import com.liepin.auth.entity.vo.resp.GetLoginHistoryRespVO;
import com.liepin.auth.entity.vo.resp.GetUserInfoRespVO;
import com.liepin.auth.entity.vo.resp.GetUsersRespVO;
import com.liepin.auth.loginlog.entity.SysLog;
import com.liepin.auth.loginlog.service.impl.SysLogServiceImpl;
import com.liepin.auth.mapper.AuthMapper;
import com.liepin.auth.service.AuthService;
import com.liepin.auth.service.base.RoleService;
import com.liepin.auth.service.base.UserService;
import com.liepin.auth.util.Crypto;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthMapper authMapper;
    private final SysLogServiceImpl sysLogService;

    @Autowired
    public AuthServiceImpl(UserService userService,RoleService roleService,AuthMapper authMapper,SysLogServiceImpl sysLogService){
        this.sysLogService = sysLogService;
        this.authMapper = authMapper;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public Result<GetUsersRespVO> getUsers(GetUsersReqVO reqVO){
        GetUsersRespVO respVO = new GetUsersRespVO();
        respVO.setList(authMapper.getUsers(reqVO));
        respVO.setTotal(authMapper.getUsersNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<GetColleaguesRespVO> getColleagues(){
        GetColleaguesRespVO respVO = new GetColleaguesRespVO();
        List<String> roles = StpUtil.getRoleList();
        if (roles.contains(RoleType.ENTERPRISE.code)){
            respVO.setList(authMapper.getColleagueList(RoleType.ENTERPRISE.code,StpUtil.getLoginIdAsLong()));
            return Result.success(respVO);
        }
        if (roles.contains(RoleType.TALENT.code)){
            respVO.setList(authMapper.getColleagueList(RoleType.TALENT.code,StpUtil.getLoginIdAsLong()));
            return Result.success(respVO);
        }
        return Result.fail();
    }

    @Override
    public Result<GetUserInfoRespVO> getUserInfo(){
        Long id = StpUtil.getLoginIdAsLong();
        GetUserInfoRespVO respVO = authMapper.getUserInfoById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(respVO), ExceptionsEnums.UserEX.ACCOUNT_NOT_FIND);
        return Result.success(respVO);
    }

    @Override
    public Result createUser(CreateUserReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getUsername()),"请填写用户名");
        AssertUtils.isFalse(reqVO.getRole() != null,"尚未分配角色");
        AssertUtils.isFalse(ObjectUtils.isEmpty(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername,reqVO.getUsername()))),"用户名重复");
        User user = User.builder()
                .username(reqVO.getUsername())
                .phone(reqVO.getPhone())
                .password(Crypto.md5("123456"))
                .roleId(roleService.getOne(new LambdaQueryWrapper<Role>()
                        .eq(Role::getRoleCode,reqVO.getRole()))
                        .getId())
                .name(reqVO.getName())
                .age(reqVO.getAge())
                .sex(reqVO.getSex())
                .remark(reqVO.getRemark())
                .createTime(TimeUtil.getNowWithMin())
                .createId(StpUtil.getLoginIdAsLong())
                .updateTime(TimeUtil.getNowWithMin())
                .updateId(StpUtil.getLoginIdAsLong())
                .build();
        userService.save(user);
        return Result.success();
    }

    @Override
    public Result updateUserInfo(UpdateUserInfoReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getUsername()),"用户名不能为空");
        AssertUtils.isFalse(reqVO.getRole() != null,"角色不能为空");
        AssertUtils.isFalse(ObjectUtils.isEmpty(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername,reqVO.getUsername()).ne(User::getId,reqVO.getId()))),"用户名重复");
        User user = User.builder()
                .id(reqVO.getId())
                .username(reqVO.getUsername())
                .phone(reqVO.getPhone())
                .roleId(roleService.getOne(new LambdaQueryWrapper<Role>()
                                .eq(Role::getRoleCode,reqVO.getRole()))
                        .getId())
                .name(reqVO.getName())
                .age(reqVO.getAge())
                .sex(reqVO.getSex())
                .remark(reqVO.getRemark())
                .updateTime(TimeUtil.getNowWithMin())
                .updateId(StpUtil.getLoginIdAsLong())
                .build();
        userService.updateById(user);
        return Result.success();
    }

    @Override
    public Result<List<Role>> getAllRoles(){
        return Result.success(roleService.list());
    }

    @Override
    public Result<GetLoginHistoryRespVO> getLoginHistory(GetLoginHistoryReqVO reqVO){
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(reqVO.getUsername()), SysLog::getUsername,reqVO.getUsername())
                .eq(ObjectUtils.isNotEmpty(reqVO.getSuccess()),SysLog::getRes,reqVO.getSuccess() ? ConstantsEnums.YESNOWAIT.YES : ConstantsEnums.YESNOWAIT.NO)
                .ge(StringUtils.isNotEmpty(reqVO.getStartTime()),SysLog::getTime,reqVO.getStartTime())
                .le(StringUtils.isNotEmpty(reqVO.getEndTime()),SysLog::getTime,reqVO.getEndTime());
        Page<SysLog> page = new Page<>(reqVO.getPage(), reqVO.getPageSize());
        sysLogService.page(page,queryWrapper);
        return Result.success(new GetLoginHistoryRespVO(page.getRecords(),page.getTotal()));
    }

    @Override
    public Result updateUserPassWord(UpdateUserPasswordReqVO reqVO){
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId,StpUtil.getLoginIdAsLong()));
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user),ExceptionsEnums.UserEX.ACCOUNT_NOT_FIND);
        if (user.getId() != StpUtil.getLoginIdAsLong())
            return Result.fail();
        if (!user.getPassword().equals(Crypto.md5(reqVO.getOldPassword())))
            return Result.fail("原密码错误,请联系管理员重置密码");
        user.setPassword(Crypto.md5(reqVO.getNewPassword()));
        userService.updateById(user);
        return Result.success();
    }

    @Override
    public Result resetPassword(Long id){
        User user = userService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user),ExceptionsEnums.UserEX.ACCOUNT_NOT_FIND);
        user.setPassword(Crypto.md5("123456"));
        userService.updateById(user);
        return Result.success("重置密码为123456");
    }

    @Override
    public Result banUser(Long id){
        User user = userService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user),ExceptionsEnums.UserEX.ACCOUNT_NOT_FIND);
        user.setStatus(ConstantsEnums.YESNOWAIT.NO.getValue());
        userService.updateById(user);
        return Result.success();
    }

    @Override
    public Result activateUser(Long id){
        User user = userService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user),ExceptionsEnums.UserEX.ACCOUNT_NOT_FIND);
        user.setStatus(ConstantsEnums.YESNOWAIT.YES.getValue());
        userService.updateById(user);
        return Result.success();
    }

    @Override
    public Result deleteUser(Long id){
        User user = userService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(user),ExceptionsEnums.UserEX.ACCOUNT_NOT_FIND);
        user.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
        userService.updateById(user);
        return Result.success();
    }
}
