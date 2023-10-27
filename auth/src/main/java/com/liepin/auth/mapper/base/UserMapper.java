package com.liepin.auth.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.auth.entity.base.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
