package com.liepin.enterprise.mapper;

import com.liepin.enterprise.entity.dto.GetEnterpriseListDTO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.resp.FollowupHistory;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnterpriseMapper {
    List<GetEnterpriseListVO> getEnterpriseOceanList(@Param("req") GetEnterpriseListReqVO reqVO);

    Long getEnterpriseOceanNum(@Param("req") GetEnterpriseListReqVO reqVO);

    void importEnterprise(String fileName);

    List<FollowupHistory> getFollowupHistory(@Param("id") Long id,@Param("page") Integer page,@Param("pageSize")Integer pageSize);
}
