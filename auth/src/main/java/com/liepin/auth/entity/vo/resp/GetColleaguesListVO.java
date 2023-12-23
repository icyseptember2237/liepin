package com.liepin.auth.entity.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetColleaguesListVO {
    @ApiModelProperty(notes = "用户id")
    private Long id;
    @ApiModelProperty(notes = "用户名")
    private String username;
    @ApiModelProperty(notes = "员工姓名")
    private String name;
}
