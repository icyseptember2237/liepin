package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PullTalentReqVO {
    @ApiModelProperty(notes = "单位公海id列表")
    private List<Long> list;
}
