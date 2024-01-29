package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.base.ContractMatch;
import com.liepin.contract.entity.vo.list.ContractMatchInfo;
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
    @ApiModelProperty(notes = "正式类型")
    private String certificateType;
    @ApiModelProperty(notes = "正书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "三类证书")
    private String threeCertificate;
    @ApiModelProperty(notes = "专业证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "证书等级")
    private String professionLevel;
    @ApiModelProperty(notes = "聘用时长")
    private String hireTime;
    @ApiModelProperty(notes = "需求人数")
    private Long requireNum;
    @ApiModelProperty(notes = "合同单价")
    private BigDecimal contractPrice;
    @ApiModelProperty(notes = "合同款项")
    private String priceType;
    @ApiModelProperty(notes = "聘用类型")
    private String hireType;
    @ApiModelProperty(notes = "出场要求")
    private String appearanceRequire;
    @ApiModelProperty(notes = "社保类型")
    private String socialInsurance;
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
    @ApiModelProperty(notes = "合同匹配状况")
    private List<ContractMatchInfo> matches;
}
