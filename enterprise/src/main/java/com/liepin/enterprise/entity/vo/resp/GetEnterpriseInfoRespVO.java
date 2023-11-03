package com.liepin.enterprise.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetEnterpriseInfoRespVO {
    private List<GetEnterpriseInfoListVO> list;
    private Long total;
}
