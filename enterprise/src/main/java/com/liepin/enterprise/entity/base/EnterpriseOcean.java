package com.liepin.enterprise.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_ocean")
public class EnterpriseOcean {
    @TableId
    private Long id;
    private String createTime;
    private Long infoId;
    private String isPrivate;
    private String dlt;
}
