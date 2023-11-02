package com.liepin.enterprise.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlterEnterpriseReqVO extends AddEnterpriseReqVO{
    @ApiModelProperty(notes = "公海id")
    private Long id;
}
