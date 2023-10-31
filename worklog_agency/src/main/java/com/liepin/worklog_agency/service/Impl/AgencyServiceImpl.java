package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.auditlog.AuditLog;
import com.liepin.common.util.auditlog.constant.TableName;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.mapper.AgencyMapper;
import com.liepin.worklog_agency.service.AgencyService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyServiceImpl extends ServiceImpl<AgencyMapper,Agency> implements AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;

    @Override
    public Result<List<Agency>> getAgency(String province, String city, String enterpriseName) {
        LambdaQueryWrapper<Agency> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!ObjectUtils.isEmpty(province)){
            lambdaQueryWrapper.eq(Agency::getProvince,province);
        }
        if(!ObjectUtils.isEmpty(city)){
            lambdaQueryWrapper.eq(Agency::getCity,city);
        }
        if(!ObjectUtils.isEmpty(enterpriseName)){
            lambdaQueryWrapper.eq(Agency::getEnterpriseName,enterpriseName);
        }
        lambdaQueryWrapper.eq(Agency::getDlt,"NO");
        lambdaQueryWrapper.eq(Agency::getAuditStatus,"YES");
        List<Agency> agencyList = agencyMapper.selectList(lambdaQueryWrapper);
        String username = agencyMapper.getUsername(StpUtil.getLoginIdAsString());

        for (Agency agency:agencyList){
            agency.setCreateId(username);
        }
        return Result.success(agencyList);
    }

    @Override
    public void insertAgency(Agency agency) {
        agency.setAuditStatus("WAIT");
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
    public void deleteAgency(List<Agency> agencyList) {
        for (Agency agency:agencyList){
            agency.setDlt("YES");
        }
        updateBatchById(agencyList);
    }

    @Override
    public void updateAgency(Agency agency) {
//        String id = agency.getId();
        updateById(agency);
    }

    @Override
    public Result<List<Agency>> getUncheckedAgency() {
        LambdaQueryWrapper<Agency> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Agency::getAuditStatus,"WAIT");
        lambdaQueryWrapper.eq(Agency::getDlt,"NO");
        List<Agency> agencyList = agencyMapper.selectList(lambdaQueryWrapper);
        for(Agency agency:agencyList){
            String username = agencyMapper.getUsername(StpUtil.getLoginIdAsString());
            agency.setCreateId(username);
        }
        return Result.success(agencyList);
    }

    @Override
    public void updateUnpassedAgency(List<Agency> agencyList) {

//        for (Agency agency:agencyList){
//            agency.setAuditStatus("YES");
//        }
        updateBatchById(agencyList);
    }
}
