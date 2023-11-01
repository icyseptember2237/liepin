package com.liepin.enterprise.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetEnterpriseListRespVO {
    List<GetEnterpriseListVO> list;
    Long total;
}
