package com.liepin.enterprise.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.enterprise.entity.base.SendTo;
import com.liepin.enterprise.entity.vo.req.GetSendAuditReqVO;
import com.liepin.enterprise.entity.vo.req.GetSendHistoryReqVO;
import com.liepin.enterprise.entity.vo.resp.GetSendListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SendToMapper extends BaseMapper<SendTo> {
    Integer checkDept(@Param("dept") String dept,@Param("toId") Long toId);

    List<GetSendListVO> selectSendHistoryList(@Param("req")GetSendHistoryReqVO reqVO, @Param("userId") Long userId);
    Long selectSendHistoryNum(@Param("req")GetSendHistoryReqVO reqVO,@Param("userId") Long userId);
    List<GetSendListVO> selectAllSendTo(@Param("req")GetSendAuditReqVO reqVO);
    Long selectAllSendToNum(@Param("req")GetSendAuditReqVO reqVO);
}
