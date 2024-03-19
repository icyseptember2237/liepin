package com.liepin.contract.entity.vo.list;

import com.liepin.contract.entity.vo.respvo.GetContractInfoRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetEnterpriseApplyMoneyAuditListVO {
    @ApiModelProperty(notes = "合同信息")
    private GetContractInfoRespVO contract;
    @ApiModelProperty(notes = "申请id")
    private Long id;
    @ApiModelProperty(notes = "申请人id")
    private Long userId;
    @ApiModelProperty(notes = "申请人姓名")
    private String userName;
    @ApiModelProperty(notes = "用途")
    private String usage;
    @ApiModelProperty(notes = "申请金额")
    private BigDecimal applyNum;
    @ApiModelProperty(notes = "审核人id")
    private Long auditId;
    @ApiModelProperty(notes = "审核人姓名")
    private String auditName;
    @ApiModelProperty(notes = "审核状态")
    private Integer status;
    @ApiModelProperty(notes = "审核时间")
    private String auditTime;
    @ApiModelProperty(notes = "申请时间")
    private String createTime;
}
