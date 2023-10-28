package com.liepin.worklog_agency.entity.response;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class WorkLogProblemRes {
    private Long id;
    private String problemInfo;
    private String solved;
    private String solution;
    private String contact;
    private String dlt;
}
