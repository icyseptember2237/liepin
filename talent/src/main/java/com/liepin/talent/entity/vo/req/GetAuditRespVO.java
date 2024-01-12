package com.liepin.talent.entity.vo.req;

import com.liepin.talent.entity.vo.list.AuditList;
import lombok.Data;

import java.util.List;

@Data
public class GetAuditRespVO {
    private List<AuditList> list;
    private Long total;
}
