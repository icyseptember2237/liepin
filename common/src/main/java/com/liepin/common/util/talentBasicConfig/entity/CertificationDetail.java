package com.liepin.common.util.talentBasicConfig.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class CertificationDetail {
    @ApiModelProperty("二建")
    private List<String> secondConstructor;
    @ApiModelProperty("一建")
    private List<String> firstConstructor;
    @ApiModelProperty("公用设备工程师")
    private List<String> publicDeviceEngineer;
    @ApiModelProperty("电气工程师")
    private List<String> electricEngineer;
    @ApiModelProperty("造价工程师")
    private List<String> priceEngineer;
    @ApiModelProperty("监理工程师")
    private List<String> supervisorEngineer;
    @ApiModelProperty("土木工程师")
    private List<String> soilWoodEngineer;
    @ApiModelProperty("结构工程师")
    private List<String> constructEngineer;
    @ApiModelProperty("注册咨询工程师")
    private List<String> registryConsoleEngineer;
    @ApiModelProperty("评估师")
    private List<String> evaluator;
    @ApiModelProperty("评估师")
    private List<String> constructor;
}

