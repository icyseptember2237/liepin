package com.liepin.talent.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("talent_match")
public class TalentMatch {
    @TableId
    private Long id;
    private Long privateId;
    private String createTime;
    private String status;
    private Long matchId;
    private Long auditId;
    private String auditTime;
    private String dlt;

}
