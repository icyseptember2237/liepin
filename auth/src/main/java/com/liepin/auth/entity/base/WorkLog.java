package com.liepin.auth.entity.base;

import lombok.Data;

@Data
public class WorkLog {
    private Long id;

    private Long userId;

    private String createTime;

    private String updateTime;

    private String dlt;
}
