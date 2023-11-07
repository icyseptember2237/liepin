package com.liepin.enterprise.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetAuditRespVO {
    private List<AuditList> list;
    private Long total;
}
