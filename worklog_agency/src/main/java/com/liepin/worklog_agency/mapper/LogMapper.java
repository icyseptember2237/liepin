package com.liepin.worklog_agency.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {

    String getWorkLog(String loginId);
}
