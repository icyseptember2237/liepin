package com.liepin.worklog_agency.entity.response;

import com.liepin.worklog_agency.entity.base.Agency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class GetAgencyRespVO {
    private List<Agency> list;
    private Long total;
}
