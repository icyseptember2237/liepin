package com.liepin.enterprise.entity.vo.req;

import lombok.Data;

@Data
public class GetFollowupReqVO {
    private String intention;
    private String province;
    private String city;
    private String county;
    private Integer page;
    private Integer pageSize;
}
