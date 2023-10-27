package com.liepin.auth.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.auth.entity.base.User;
import com.liepin.auth.mapper.base.UserMapper;
import com.liepin.auth.service.base.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
