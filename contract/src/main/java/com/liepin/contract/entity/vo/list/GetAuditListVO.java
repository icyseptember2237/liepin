package com.liepin.contract.entity.vo.list;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetAuditListVO {
    @ApiModelProperty(notes = "合同id")
    private Long contractId;
    @ApiModelProperty(notes = "员工id")
    private Long userId;
    @ApiModelProperty(notes = "员工姓名")
    private String username;
    @ApiModelProperty(notes = "单位私库id")
    private Long privateId;
    @ApiModelProperty(notes = "单位名称")
    private String enterpriseName;
    @ApiModelProperty(notes = "是否上传合同")
    private String uploadContract;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
    @ApiModelProperty(notes = "合同匹配状态")
    private List<ContractMatchInfo> matchInfos;
    @ApiModelProperty(notes = "审核时间")
    private String auditTime;
    @ApiModelProperty(notes = "审核状态")
    private String status;
    @ApiModelProperty(notes = "审核人id")
    private Long auditId;
    @ApiModelProperty(notes = "审核人姓名")
    private String auditName;
    @ApiModelProperty(notes = "审核备注")
    private String remark;

}
