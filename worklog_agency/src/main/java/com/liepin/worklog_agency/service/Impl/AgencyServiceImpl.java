package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.auditlog.AuditLog;
import com.liepin.common.util.auditlog.constant.TableName;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.AddAgencyReqVO;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.entity.request.GetAgencyReqVO;
import com.liepin.worklog_agency.entity.request.UpdateAgencyReqVO;
import com.liepin.worklog_agency.entity.response.GetAgencyRespVO;
import com.liepin.worklog_agency.entity.response.GetWorkLogRespVO;
import com.liepin.worklog_agency.mapper.AgencyMapper;
import com.liepin.worklog_agency.service.AgencyService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyServiceImpl extends ServiceImpl<AgencyMapper,Agency> implements AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;

    @Override
    public Result<GetAgencyRespVO> getAgencyList(GetAgencyReqVO reqVO) {
        GetAgencyRespVO respVO = new GetAgencyRespVO();
        if("ALL".equals(reqVO.getAuditStatus())){
            reqVO.setAuditStatus("");
        }
        respVO.setList(agencyMapper.getAgenctList(reqVO));
        respVO.setTotal(agencyMapper.getAgencyNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    public void addAgency(AddAgencyReqVO reqVO) {

        Agency agency = new Agency();
        BeanUtils.copyProperties(reqVO,agency);
//        agency.setId();
        agency.setAuditStatus(ConstantsEnums.AuditStatus.WAIT.getStatus());
        agency.setCreateId(StpUtil.getLoginIdAsString());
        agency.setCreateTime(TimeUtil.getNowWithMin());
        save(agency);

        LambdaQueryWrapper<Agency> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Agency::getId).last("limit 1");
        String id = agencyMapper.selectOne(lambdaQueryWrapper).getId();
        System.out.println(id);
        AuditLog.setAuditFail(TableName.AGENCY, Long.valueOf(id));

    }

    @Override
    public void deleteAgency(Long id) {
        Agency agency = getById(id);
        agency.setDlt(ConstantsEnums.YESNO.YES.getValue());
        updateById(agency);
    }

    @Override
    public void updateAgency(UpdateAgencyReqVO reqVO) {

        Agency agency = new Agency();
        BeanUtils.copyProperties(reqVO,agency);
        agency.setCreateTime(TimeUtil.getNowWithMin());
        updateById(agency);
    }



    @Override
    public void updateUnpassedAgency(List<Agency> agencyList) {


        updateBatchById(agencyList);
    }

    @Override
    public Result rejectAgency(Long id) {
        Agency agency = agencyMapper.selectById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(agency), ExceptionsEnums.AgencyEX.AGENCY_NOT_FOUND);
        agency.setAuditStatus(ConstantsEnums.AuditStatus.FAIL.getStatus());
        updateById(agency);
        return Result.success();
    }

    @Override
    public Result passAgency(Long id) {
        Agency agency = agencyMapper.selectById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(agency), ExceptionsEnums.AgencyEX.AGENCY_NOT_FOUND);
        agency.setAuditStatus(ConstantsEnums.AuditStatus.PASS.getStatus());
        updateById(agency);
        return Result.success();
    }
}
