package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetSendAuditReqVO {
    @ApiModelProperty(notes = "人才名称")
    private String talentName;
    @ApiModelProperty(notes = "审核状态 PASS/WAIT/FAIL")
    private String status;
    @ApiModelProperty(notes = "送出人id")
    private Long fromId;
    @ApiModelProperty(notes = "接收人id")
    private Long toId;
    private Integer page;
    private Integer pageSize;

}
