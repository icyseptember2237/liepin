package com.liepin.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("basic_config")
public class BasicConfig {
    @TableId
    private Long id;

    private String valueName;

    private Long parentId;

    private String dlt;

}
