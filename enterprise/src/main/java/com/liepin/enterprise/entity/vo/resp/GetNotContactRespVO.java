package com.liepin.enterprise.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetNotContactRespVO {
    private List<GetNotContactList> list;
    private Long total;
}
