package com.liepin.contract.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_contract")
public class EnterpriseContract {
    private Long id;
    private Long privateId;
    private Long userId;
    private String certificateType;
    private String certificateMajor;
    private String threeCertificate;
    private String professionCertificate;
    private String professionLevel;
    private String hireTime;
    private Long requireNum;
    private Long contractPrice;
    private String priceType;
    private String hireType;
    private String appearanceRequire;
    private String socialInsurance;
    private String remark;
    private String uploadContract;
    private String contractLink;
    private String status;
    private String createTime;
    private String dlt;
}
