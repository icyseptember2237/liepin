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
    private String county;
    @ExcelProperty(value = "企业邮箱",index = 3)
    private String email;
    private String qualification;
    @ExcelProperty(value = "法人姓名",index = 5)
    private String legalRepresentative;
    @ExcelProperty(value = "联系人",index = 1)
    private String contact;
    @ExcelProperty(value = "联系电话",index = 2)
    private String phone;
    @ExcelProperty(value = "地址",index = 4)
    private String address;
    private String remark;
    private String createTime;
    private String isPrivate;
    private String dlt;
}
