package com.liepin.enterprise.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_throwback_history")
public class EnterpriseThrowbackHistory {
    @TableId
    private Long id;
    private Long infoId;
    private Long privateId;
    private String createTime;
    private String auditStatus;
    private String auditTime;
    private String throwbackReason;
    private String remark;

}
