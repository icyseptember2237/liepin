package com.liepin.auth.config;

import cn.dev33.satoken.stp.StpInterface;
import com.liepin.auth.entity.base.Role;
import com.liepin.auth.mapper.LoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SaTokenAuth implements StpInterface {

    @Autowired
    private static LoginMapper loginMapper;
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        List<Role> roles = loginMapper.getUserRole((Long) loginId);
        roles.forEach((role) -> {
            list.add(role.getRoleCode());
        });
        return list;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        return list;
    }
}
