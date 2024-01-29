package com.liepin.contract.entity.vo.list;

import com.liepin.contract.entity.base.ContractMatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContractMatchInfo extends ContractMatch {
    @ApiModelProperty(notes = "匹配人姓名")
    private String username;
    @ApiModelProperty(notes = "人才姓名")
    private String talentName;
}
