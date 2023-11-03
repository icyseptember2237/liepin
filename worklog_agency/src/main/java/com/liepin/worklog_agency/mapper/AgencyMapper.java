package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.entity.request.GetAgencyReqVO;
import com.liepin.worklog_agency.entity.response.GetAgencyRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgencyMapper extends BaseMapper<Agency> {
    String getUsername(String id);

    List<Agency> getAgenctList(@Param("req") GetAgencyReqVO reqVO);

    Long getAgencyNum(@Param("req") GetAgencyReqVO respVO);

    List<Agency> getSelfAgenctList(@Param("req") GetAgencyReqVO reqVO);
}
