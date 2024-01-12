package com.liepin.talent.entity.vo.resp;

import com.liepin.talent.entity.vo.list.GetNotContactList;
import lombok.Data;

import java.util.List;

@Data
public class GetNotContactRespVO {
    private List<GetNotContactList> list;
    private Long total;
}
