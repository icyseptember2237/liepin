package com.liepin.worklog_agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogProblemMapper extends BaseMapper<WorkLogProblem> {
}
