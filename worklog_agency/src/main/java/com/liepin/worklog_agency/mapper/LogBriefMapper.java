package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.response.WorkLogBriefRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogBriefMapper extends BaseMapper<WorkLogBriefRes> {
}
