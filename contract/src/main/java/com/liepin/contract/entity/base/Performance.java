package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("performance")
public class Performance {
    @TableId
    private Long id;
    private Long userId;
    private String role;
    private Long performanceAmount;
    private Long contractId;
    private Long enterprisePrivateId;
    private Long requireId;
    private Long matchId;
    private Long talentPrivateId;
    private String createTime;
    private String dlt;
}
