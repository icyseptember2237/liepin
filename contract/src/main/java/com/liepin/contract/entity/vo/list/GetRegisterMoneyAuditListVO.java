package com.liepin.contract.entity.vo.list;

import com.liepin.contract.entity.vo.respvo.GetContractInfoRespVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetRegisterMoneyAuditListVO {
    @ApiModelProperty(value = "合同信息")
    private GetContractInfoRespVO contract;
    @ApiModelProperty(value = "认款id")
    private Long id;
    @ApiModelProperty(value = "合同id")
    private Long contractId;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "认款金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "距离合同总价还差的金额")
    private BigDecimal restFromTotal;
    @ApiModelProperty(value = "审核状态")
    private String status;
    @ApiModelProperty(value = "审核id")
    private Long auditId;
    @ApiModelProperty(value = "审核人")
    private String auditName;
    @ApiModelProperty(value = "审核备注")
    private String remark;
    @ApiModelProperty(value = "审核时间")
    private String auditTime;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
