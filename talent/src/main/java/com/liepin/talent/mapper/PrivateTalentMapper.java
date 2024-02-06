package com.liepin.talent.mapper;

import com.liepin.talent.entity.vo.list.AuditList;
import com.liepin.talent.entity.vo.list.GetFollowupListVO;
import com.liepin.talent.entity.vo.list.GetNotContactList;
import com.liepin.talent.entity.vo.req.GetAuditReqVO;
import com.liepin.talent.entity.vo.req.GetFollowupReqVO;
import com.liepin.talent.entity.vo.req.GetNotContactReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PrivateTalentMapper {
    List<GetNotContactList> selectNotContactList(@Param("userId") Long userId, @Param("req") GetNotContactReqVO reqVO);

    Long selectNotContactListNum(@Param("userId") Long userId,@Param("req") GetNotContactReqVO reqVO);

    List<AuditList> selectAuditList(@Param("req") GetAuditReqVO reqVO, @Param("userId") Long userId);

    Long selectAuditListNum(@Param("req") GetAuditReqVO reqVO,@Param("userId") Long userId);

    List<GetFollowupListVO> selectFollowupList(@Param("req") GetFollowupReqVO reqVO, @Param("userId") Long userId);

    Long selectFollowupListNum(@Param("req") GetFollowupReqVO reqVO,@Param("userId") Long userId);

    List<GetFollowupListVO> selectAllFollowupList(@Param("userId") Long userId);

}
