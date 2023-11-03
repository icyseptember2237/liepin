package com.liepin.auth.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetLoginHistoryReqVO {
    @ApiModelProperty(notes = "用户名")
    private String username;
    @ApiModelProperty(notes = "是否成功登录")
    private Boolean success;
    @ApiModelProperty(notes = "起始时间")
    private String startTime;
    @ApiModelProperty(notes = "结束时间")
    private String endTime;
    private Integer page;
    private Integer pageSize;
}
