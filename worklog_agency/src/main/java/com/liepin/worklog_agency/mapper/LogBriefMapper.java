package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.request.GetWorkLogReqVO;
import com.liepin.worklog_agency.entity.response.WorkLogBriefRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogBriefMapper extends BaseMapper<WorkLogBriefRes> {
    List<WorkLogBriefRes> getAllBriefLog(@Param("req") GetWorkLogReqVO reqVO);

    Long getAllBriefLogNum(@Param("req") GetWorkLogReqVO reqVO);

//    Long getProblemNum(@Param("req") GetWorkLogReqVO reqVO,String dayTime);
}
