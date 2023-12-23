package com.liepin.enterprise.mapper;

import com.liepin.enterprise.entity.vo.req.GetFollowupReqVO;
import com.liepin.enterprise.entity.vo.req.GetNotContactReqVO;
import com.liepin.enterprise.entity.vo.req.GetAuditReqVO;
import com.liepin.enterprise.entity.vo.resp.GetFollowupListVO;
import com.liepin.enterprise.entity.vo.resp.GetNotContactList;
import com.liepin.enterprise.entity.vo.resp.AuditList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PrivateEnterpriseMapper {

    List<GetNotContactList> selectNotContactList(@Param("userId") Long userId,@Param("req") GetNotContactReqVO reqVO);

    Long selectNotContactListNum(@Param("userId") Long userId,@Param("req") GetNotContactReqVO reqVO);

    List<AuditList> selectAuditList(@Param("req") GetAuditReqVO reqVO,@Param("userId") Long userId);

    Long selectAuditListNum(@Param("req") GetAuditReqVO reqVO,@Param("userId") Long userId);

    List<GetFollowupListVO> selectFollowupList(@Param("req") GetFollowupReqVO reqVO,@Param("userId") Long userId);

    Long selectFollowupListNum(@Param("req") GetFollowupReqVO reqVO,@Param("userId") Long userId);

}
