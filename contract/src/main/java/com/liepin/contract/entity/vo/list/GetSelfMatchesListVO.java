package com.liepin.contract.entity.vo.list;

import com.liepin.contract.entity.vo.respvo.GetContractInfoRespVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetSelfMatchesListVO {
    @ApiModelProperty(notes = "合同信息、需求和人才匹配的信息还有钱都在里面，跟着下面的id找，不想再去另写一个接口了")
    GetContractInfoRespVO contract;
    private Long matchId;
    private Long contractId;
    private Long requireId;
    private Long talentId;
}
