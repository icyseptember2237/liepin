package com.liepin.contract.entity.vo.reqvo;

import com.liepin.contract.entity.vo.list.MatchTalent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class MatchReqVO {
    @ApiModelProperty(notes = "合同id")
    private Long contractId;
    @ApiModelProperty(notes = "需求id")
    private Long requireId;
    @ApiModelProperty(notes = "人才数组")
    private List<MatchTalent> talents;
}
