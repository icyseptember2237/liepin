package com.liepin.auth.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetUsersRespVO {
    private List<GetUsersListVO> list;
    private Long total;
}
