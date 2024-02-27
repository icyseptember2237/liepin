package com.liepin.contract.entity.vo.reqvo;

import com.liepin.contract.entity.vo.list.NewContractRequireListVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NewContractReqVO {
    @ApiModelProperty(notes = "单位私库id")
    private Long privateId;
    @ApiModelProperty(notes = "合同款项 全额/预付")
    private String priceType;
    @ApiModelProperty(notes = "合同总价 每个需求中的价格加其他费用")
    private BigDecimal totalPrice;
    @ApiModelProperty(notes = "需求总数")
    private Long totalRequireNum;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "合同号")
    private String contractNumber;
    @ApiModelProperty(notes = "证书需求")
    private List<NewContractRequireListVO> requires;
}
