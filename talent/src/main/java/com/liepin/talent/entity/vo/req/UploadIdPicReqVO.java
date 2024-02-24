package com.liepin.talent.entity.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadIdPicReqVO {
    @ApiModelProperty(notes = "私库id")
    private Long Id;
    @ApiModelProperty(notes = "正面url")
    private String frontUrl;
    @ApiModelProperty(notes = "背面url")
    private String backUrl;

}
