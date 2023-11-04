package com.liepin.talent.entity.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class GetTalentListRespVO {
    private List<GetTalentListVO> list;
    private Long total;
}
