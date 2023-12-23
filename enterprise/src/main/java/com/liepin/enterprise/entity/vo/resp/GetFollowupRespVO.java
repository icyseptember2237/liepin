package com.liepin.enterprise.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetFollowupRespVO {
    private List<GetFollowupListVO> list;
    private Long total;
}
