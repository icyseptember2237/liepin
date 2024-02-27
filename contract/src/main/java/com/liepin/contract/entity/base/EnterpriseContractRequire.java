package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_contract_require")
public class EnterpriseContractRequire {
    @TableId
    private Long id;
    private Long contractId;
    private Long privateId;
    private Long userId;
    private String certificateType;
    private String certificateMajor;
    private String professionCertificate;
    private String professionLevel;
    private String nineMember;
    private String threeCertificate;
    private String hireType;
    private Integer hireTime;
    private String appearanceRequire;
    private String socialInsurance;
    private Long price;
    private Long otherPrice;
    private Long requireNum;
    private Long matchedNum;
    private String status;
    private String dlt;
}
