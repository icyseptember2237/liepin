package com.liepin.contract.entity.vo.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetSelfMatchesReqVO {
    private Long page;
    private Long pageSize;
    @ApiModelProperty(notes = "审核状态 MATCHING UNFINISHED FINISHED")
    private String status;
}
