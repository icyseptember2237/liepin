package com.liepin.enterprise.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FollowupHistory {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "跟进人Id")
    private Long followupId;
    @ApiModelProperty(notes = "跟进人")
    private String followup;
    @ApiModelProperty(notes = "跟进时间")
    private String time;
    @ApiModelProperty(notes = "下次联系时间")
    private String nextTime;
    @ApiModelProperty(notes = "跟进详情")
    private String remark;
}
