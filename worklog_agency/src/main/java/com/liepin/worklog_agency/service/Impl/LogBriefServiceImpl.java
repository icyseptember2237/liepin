package com.liepin.worklog_agency.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.auth.entity.base.Role;
import com.liepin.auth.mapper.base.RoleMapper;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.request.GetWorkLogReqVO;
import com.liepin.worklog_agency.entity.response.GetWorkLogRespVO;
import com.liepin.worklog_agency.entity.response.WorkLogBriefRes;
import com.liepin.worklog_agency.mapper.LogBriefMapper;
import com.liepin.worklog_agency.service.LogBriefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogBriefServiceImpl extends ServiceImpl<LogBriefMapper,WorkLogBriefRes> implements LogBriefService{
    @Autowired
    private LogBriefMapper logBriefMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public Result<GetWorkLogRespVO> getAllWork(GetWorkLogReqVO reqVO) {
        GetWorkLogRespVO respVO = new GetWorkLogRespVO();

        if("ALL".equals(reqVO.getRole())){
            reqVO.setRole("");
        } else {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getRoleCode,reqVO.getRole());
        Role role = roleMapper.selectOne(lambdaQueryWrapper);
        Long id = role.getId();
        reqVO.setRole(String.valueOf(id));
         }
        if ("ALL".equals(reqVO.getSolved())){
            reqVO.setSolved("");
        }
        respVO.setList(logBriefMapper.getAllBriefLog(reqVO));
        respVO.setTotal(logBriefMapper.getAllBriefLogNum(reqVO));
        return Result.success(respVO);
    }
}
