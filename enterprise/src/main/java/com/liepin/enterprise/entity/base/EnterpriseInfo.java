package com.liepin.enterprise.entity.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("enterprise_info")
public class EnterpriseInfo {
    private Long id;
    @ExcelProperty(value = "单位名称",index = 0)
    private String name;
    private String intention;
    private String registeredCapital;
    private String province;
    private String city;
    private String email;
    private String qualification;
    @ExcelProperty(value = "法人姓名",index = 3)
    private String legalRepresentative;
    private String contact;
    @ExcelProperty(value = "联系方式",index = 1)
    private String phone;
    @ExcelProperty(value = "地址",index = 2)
    private String address;
    private String remark;
}
