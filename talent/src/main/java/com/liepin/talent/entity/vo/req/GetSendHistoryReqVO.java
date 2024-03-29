package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetSendHistoryReqVO {
    @ApiModelProperty(notes = "审核状态 PASS/WAIT/FAIL")
    private String status;
    @ApiModelProperty(notes = "内推对象id")
    private Long toId;
    @ApiModelProperty(notes = "人才名称")
    private String talentName;
    @ApiModelProperty(notes = " true查询自己送出记录 false查询接收记录")
    private Boolean isSend;
    private Integer page;
    private Integer pageSize;
}
