package com.liepin.enterprise.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.enterprise.entity.base.SendTo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SendToMapper extends BaseMapper<SendTo> {
    Integer checkDept(@Param("dept") String dept,@Param("toId") Long toId);
}
