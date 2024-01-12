package com.liepin.talent.entity.vo.resp;

import com.liepin.talent.entity.vo.list.GetFollowupListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetFollowupRespVO {
    private List<GetFollowupListVO> list;
    private Long total;
}
