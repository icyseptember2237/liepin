package com.liepin.enterprise.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetEnterpriseListDTO {
    private Long num;
    private Long id;
    private String name;
    private String province;
    private String city;
    private String legalRepresentative;
    private String createTime;
    private String throwbackReason;
}
