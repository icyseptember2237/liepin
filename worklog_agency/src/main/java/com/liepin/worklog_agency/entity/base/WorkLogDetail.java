package com.liepin.worklog_agency.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "work_log_detail")
public class WorkLogDetail {
    @TableId
    private Long id;
    private Long logId;
    private Integer wechatNum;
    private Integer phoneNum;
    private Integer intentionalCustomer;
    private String workSituation;
    private String workSummary;
    private String workPlan;

}
