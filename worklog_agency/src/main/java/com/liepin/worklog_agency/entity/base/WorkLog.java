package com.liepin.worklog_agency.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "work_log")
public class WorkLog {
    @TableId
    private Long id;

    private Long userId;

    private String createTime;

    private String updateTime;

    private String dlt;

}
