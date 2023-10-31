package com.liepin.worklog_agency.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Agency {
    @TableId
    @ApiModelProperty(notes = "id")
    private String id;
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
    @ApiModelProperty(notes = "是否删除")
    private String dlt;
    @ApiModelProperty(notes = "是否通过")
    private String auditStatus;
    @ApiModelProperty(notes = "创建时间")
    private String createTime;

}
