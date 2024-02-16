package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetSendListVO {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "部门")
    private String dept;
    @ApiModelProperty(notes = "单位私库id")
    private Long privateId;
    @ApiModelProperty(notes = "单位名称")
    private String enterpriseName;
    @ApiModelProperty(notes = "送出人id")
    private Long fromId;
    @ApiModelProperty(notes = "送出人姓名")
    private String fromName;
    @ApiModelProperty(notes = "接收人id")
    private Long toId;
    @ApiModelProperty(notes = "接收人姓名")
    private String toName;
    @ApiModelProperty(notes = "送出备注")
    private String remark;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
    @ApiModelProperty(notes = "审核状态")
    private String status;
    @ApiModelProperty(notes = "审核人id")
    private Long auditId;
    @ApiModelProperty(notes = "审核人姓名")
    private String auditName;
    @ApiModelProperty(notes = "审核备注")
    private String auditRemark;
}
