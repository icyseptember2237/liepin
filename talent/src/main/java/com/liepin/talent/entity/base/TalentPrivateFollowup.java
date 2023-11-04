package com.liepin.talent.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("talent_private_followup")
public class TalentPrivateFollowup {
    @TableId
    private Long id;
    private Long privateId;
    private Long followupId;
    private String time;
    private String nextTime;
    private String remark;
    private String dlt;
}
