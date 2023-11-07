package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditList {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "单位名称")
    private String name;
    @ApiModelProperty(notes = "扔回原因")
    private String throwbackReason;
    @ApiModelProperty(notes = "扔回备注")
    private String remark;
    @ApiModelProperty(notes = "用户id")
    private Long userId;
    @ApiModelProperty(notes = "用户姓名")
    private String username;
    @ApiModelProperty(notes = "送审时间")
    private String sendAuditTime;
    @ApiModelProperty(notes = "审核状态")
    private String auditStatus;
    @ApiModelProperty(notes = "审核时间")
    private String auditTime;
}
