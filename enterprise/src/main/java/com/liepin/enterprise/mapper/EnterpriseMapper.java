package com.liepin.enterprise.mapper;

import com.liepin.enterprise.entity.dto.GetEnterpriseListDTO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnterpriseMapper {
    List<GetEnterpriseListDTO> getEnterpriseOceanList(@Param("req") GetEnterpriseListReqVO reqVO);

    Long getEnterpriseOceanNum(@Param("req") GetEnterpriseListReqVO reqVO);
}
