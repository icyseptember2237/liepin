package com.liepin.auth.entity.dto;

import com.liepin.auth.entity.base.User;
import lombok.Data;

@Data
public class LoginUser extends User {
    private String roleName;
    private String roleCode;
}
