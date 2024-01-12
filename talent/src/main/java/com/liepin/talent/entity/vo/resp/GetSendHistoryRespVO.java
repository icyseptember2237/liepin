package com.liepin.talent.entity.vo.resp;

import com.liepin.talent.entity.vo.list.GetSendListVO;
import lombok.Data;

import java.util.List;

@Data
public class GetSendHistoryRespVO {
    private List<GetSendListVO> list;
    private Long total;
}
