package com.liepin.common.util.operationLog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("operation_log")
public class OperationLog {
    @TableId
    private Long id;
    @ApiModelProperty(notes = "操作时间")
    private String createTime;
    @ApiModelProperty(notes = "操作人姓名")
    private String operatorName;
    @ApiModelProperty(notes = "操作人ID")
    private Long operatorId;
    @ApiModelProperty(notes = "操作人IP")
    private String operationIp;
    @ApiModelProperty(notes = "操作具体内容")
    private String operationDetail;
    @ApiModelProperty(notes = "操作模块")
    private String module;
    @ApiModelProperty(notes = "操作类型")
    private String type;
    @ApiModelProperty(notes = "操作状态")
    private String operationStatus;
}
