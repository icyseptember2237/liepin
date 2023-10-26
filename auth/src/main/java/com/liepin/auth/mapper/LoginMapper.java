package com.liepin.auth.mapper;

import com.liepin.auth.entity.base.Role;
import com.liepin.auth.entity.base.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper {

    User getUserByUsername(@Param("username") String username);

    List<Role> getUserRole(@Param("userId") Long userId);

}
