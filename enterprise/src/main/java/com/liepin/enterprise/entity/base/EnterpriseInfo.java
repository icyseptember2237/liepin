package com.liepin.enterprise.entity.base;

import lombok.Data;

@Data
public class EnterpriseInfo {
    private Long id;
    private String name;
    private String intention;
    private String registeredCapital;
    private String province;
    private String city;
    private String email;
    private String qualification;
    private String legalRepresentative;
    private String contact;
    private String phone;
    private String address;
    private String remark;
}
