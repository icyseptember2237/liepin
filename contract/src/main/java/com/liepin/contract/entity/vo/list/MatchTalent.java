package com.liepin.contract.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchTalent {
    @ApiModelProperty(notes = "人才私库id")
    private Long id;
    @ApiModelProperty(notes = "人才费用")
    private BigDecimal price;
}
