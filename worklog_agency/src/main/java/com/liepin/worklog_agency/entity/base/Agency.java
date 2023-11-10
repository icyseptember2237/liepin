package com.liepin.worklog_agency.entity.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Agency {
    @TableId
    @ApiModelProperty(notes = "id")
    private Long id;

    @ApiModelProperty(notes = "企业名称")
    @ExcelProperty(value = "企业名称", index = 0)
    private String enterpriseName;

    @ApiModelProperty(notes = "企业法人")
    @ExcelProperty(value = "企业法人", index = 1)
    private String person;

    @ApiModelProperty(notes = "省")
    @ExcelProperty(value = "省", index = 2)
    private String province;

    @ApiModelProperty(notes = "市")
    @ExcelProperty(value = "市", index = 3)
    private String city;

    @ApiModelProperty(notes = "详细地址")
    @ExcelProperty(value = "详细地址", index = 4)
    private String detailAddr;

    @ApiModelProperty(notes = "创建员工")
    private String createId;

    @ApiModelProperty(notes = "联系电话")
    @ExcelProperty(value = "联系电话", index = 5)
    private String tel;

    @ApiModelProperty(notes = "是否通过(YES通过WAIT审核NO删除)")
    private String auditStatus;

    @ApiModelProperty(notes = "创建时间")
    private String createTime;

    private String dlt;
    private String remark;
}
