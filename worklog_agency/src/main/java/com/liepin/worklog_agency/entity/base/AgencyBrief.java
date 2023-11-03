package com.liepin.worklog_agency.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AgencyBrief {
    @TableId
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "企业名称")
    private String enterpriseName;
    @ApiModelProperty(notes = "企业法人")
    private String person;
    @ApiModelProperty(notes = "省")
    private String province;
    @ApiModelProperty(notes = "市")
    private String city;
    @ApiModelProperty(notes = "详细地址")
    private String detailAddr;
    @ApiModelProperty(notes = "创建员工")
    private String createId;
    @ApiModelProperty(notes = "联系电话")
    private String tel;
    @ApiModelProperty(notes = "是否通过(YES通过WAIT审核NO删除)")
    private String auditStatus;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "备注")
    private String remark;
}
