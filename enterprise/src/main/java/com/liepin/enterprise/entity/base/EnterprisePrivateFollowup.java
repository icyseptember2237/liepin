package com.liepin.enterprise.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_private_followup")
public class EnterprisePrivateFollowup {
    @TableId
    private Long id;
    private Long privateId;
    private Long followupId;
    private String time;
    private String nextTime;
    private String remark;
    private String dlt;
}
