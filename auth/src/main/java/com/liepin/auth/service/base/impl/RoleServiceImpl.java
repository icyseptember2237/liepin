package com.liepin.auth.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.auth.entity.base.Role;
import com.liepin.auth.mapper.base.RoleMapper;
import com.liepin.auth.service.base.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
