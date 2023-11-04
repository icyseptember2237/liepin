package com.liepin.talent.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("talent_private")
public class TalentPrivate {
    @TableId
    private Long id;
    private Long userId;
    private Long infoId;
    private String createTime;
    private String status;
    private String throwback;
    private String dlt;
}
