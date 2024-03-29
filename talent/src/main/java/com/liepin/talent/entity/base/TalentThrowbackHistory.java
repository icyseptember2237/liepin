package com.liepin.talent.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("talent_throwback_history")
public class TalentThrowbackHistory {
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
