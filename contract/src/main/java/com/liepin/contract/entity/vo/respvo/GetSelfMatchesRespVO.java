package com.liepin.contract.entity.vo.respvo;

import com.liepin.contract.entity.vo.list.GetSelfMatchesListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetSelfMatchesRespVO {
    private List<GetSelfMatchesListVO> list;
    private Long total;
}
