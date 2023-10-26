package com.liepin.worklog_agency.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "work_log_problem")
public class WorkLogProblem {
    @TableId
    private Long id;
    private Long detaiId;
    private String problemInfo;
    private String solved;
    private String solution;
    private String contact;
    private String dlt;
}
