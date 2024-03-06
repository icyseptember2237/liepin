package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.base.ContractMatch;
import com.liepin.contract.entity.vo.list.ContractMatchInfo;
import com.liepin.contract.entity.vo.list.ContractRequireListVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GetContractInfoRespVO {
    @ApiModelProperty(notes = "合同id")
    private Long id;
    @ApiModelProperty(notes = "公司私库id")
    private Long privateId;
    @ApiModelProperty(notes = "公司名称")
    private String enterpriseName;
    @ApiModelProperty(notes = "合同归属人id")
    private Long userId;
    @ApiModelProperty(notes = "合同归属人姓名")
    private String username;
    @ApiModelProperty(notes = "合同总价")
    private BigDecimal totalPrice;
    @ApiModelProperty(notes = "总需求数")
    private Long totalRequireNum;
    @ApiModelProperty(notes = "合同款项")
    private String priceType;
    @ApiModelProperty(notes = "备注")
    private String remark;
    @ApiModelProperty(notes = "是否上传合同文件")
    private String uploadContract;
    @ApiModelProperty(notes = "合同文件链接")
    private String contractLink;
    @ApiModelProperty(notes = "合同状态")
    private String status;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
    @ApiModelProperty(notes = "是否删除")
    private String dlt;
    @ApiModelProperty(notes = "合同下需求")
    private List<ContractRequireListVO> requires;

}
