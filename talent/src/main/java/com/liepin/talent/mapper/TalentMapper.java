package com.liepin.talent.mapper;

import com.liepin.talent.entity.vo.req.GetTalentListReqVO;
import com.liepin.talent.entity.vo.resp.GetTalentListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TalentMapper {
    List<GetTalentListVO> getTalentList(@Param("req")GetTalentListReqVO reqVO);

    Long getTalentListNum(@Param("req")GetTalentListReqVO reqVO);

    void importTalent(@Param("fileName") String fileName);
}
