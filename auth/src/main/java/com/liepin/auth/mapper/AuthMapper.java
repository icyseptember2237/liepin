package com.liepin.auth.mapper;

import com.liepin.auth.entity.vo.req.GetUsersReqVO;
import com.liepin.auth.entity.vo.resp.GetUsersListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthMapper {

    List<GetUsersListVO> getUsers(@Param("req") GetUsersReqVO reqVO);

    Long getUsersNum(@Param("req") GetUsersReqVO reqVO);
}
