package com.liepin.auth.entity.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private Long id;
    private String roleName;
    private String roleCode;
    private String createTime;
    private String dlt;
}
