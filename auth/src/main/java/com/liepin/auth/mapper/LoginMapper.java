package com.liepin.auth.mapper;

import com.liepin.auth.entity.base.Role;
import com.liepin.auth.entity.base.User;
import com.liepin.auth.entity.dto.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper {

    LoginUser getUserByUsername(@Param("username") String username);

    List<Role> getUserRole(@Param("userId") Long userId);

    Boolean commitWorkLog(@Param("userId") Long userId);

}
