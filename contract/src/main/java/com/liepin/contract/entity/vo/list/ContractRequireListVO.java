package com.liepin.contract.entity.vo.list;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ContractRequireListVO {
    @ApiModelProperty(notes = "需求id")
    private Long id;
    @ApiModelProperty(notes = "合同id")
    private Long contractId;
    @ApiModelProperty(notes = "公司id")
    private Long privateId;
    @ApiModelProperty(notes = "公司名称")
    private String enterpriseName;
    @ApiModelProperty(notes = "合同所属用户id")
    private Long userId;
    @ApiModelProperty(notes = "合同所属用户姓名")
    private String username;
    @ApiModelProperty(notes = "证书类型")
    private String certificateType;
    @ApiModelProperty(notes = "证书专业")
    private String certificateMajor;
    @ApiModelProperty(notes = "职称证书")
    private String professionCertificate;
    @ApiModelProperty(notes = "证书等级")
    private String professionLevel;
    @ApiModelProperty(notes = "九大员")
    private String nineMember;
    @ApiModelProperty(notes = "三类证书")
    private String threeCertificate;
    @ApiModelProperty(notes = "聘用类型")
    private String hireType;
    @ApiModelProperty(notes = "聘用时间")
    private Integer hireTime;
    @ApiModelProperty(notes = "出场要求")
    private String appearanceRequire;
    @ApiModelProperty(notes = "社保地区")
    private String socialInsurance;
    @ApiModelProperty(notes = "需求单价")
    private BigDecimal price;
    @ApiModelProperty(notes = "需求数量")
    private Long requireNum;
    @ApiModelProperty(notes = "已匹配数量")
    private Long matchedNum;
    private String status;
    @ApiModelProperty(notes = "需求匹配状况")
    private List<ContractMatchInfo> matches;
}
