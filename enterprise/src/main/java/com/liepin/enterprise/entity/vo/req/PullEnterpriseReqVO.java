package com.liepin.enterprise.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PullEnterpriseReqVO {
    @ApiModelProperty(notes = "单位公海id列表")
    private List<Long> list;
}
