package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetContractsByPrivateIdListVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GetContractsByPrivateIdRespVO {
    private List<GetContractsByPrivateIdListVO> list;
    @ApiModelProperty(notes = "合同总价")
    private BigDecimal totalPrice;
}

