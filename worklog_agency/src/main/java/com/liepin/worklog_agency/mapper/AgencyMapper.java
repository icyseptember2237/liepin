package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.base.Agency;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgencyMapper extends BaseMapper<Agency> {
    String getUsername(String id);
}
