package com.liepin.talent.entity.vo.resp;

import com.liepin.talent.entity.vo.list.GetAllFollowupListVO;
import com.liepin.talent.entity.vo.list.GetFollowupListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetAllFollowupRespVO {
    private List<GetAllFollowupListVO> list;
}
