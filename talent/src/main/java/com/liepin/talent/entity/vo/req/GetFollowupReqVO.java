package com.liepin.talent.entity.vo.req;

import lombok.Data;

@Data
public class GetFollowupReqVO {
    private String intention;
    private String name;
    private String phone;
    private String sex;
    private Integer page;
    private Integer pageSize;
}
