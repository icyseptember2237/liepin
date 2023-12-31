package com.liepin.worklog_agency.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.auth.entity.base.User;
import com.liepin.auth.mapper.base.UserMapper;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.auditlog.AuditLog;
import com.liepin.common.util.auditlog.constant.TableName;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.entity.base.*;
import com.liepin.worklog_agency.entity.request.GetAgencyReqVO;
import com.liepin.worklog_agency.entity.request.UpdateAgencyReqVO;
import com.liepin.worklog_agency.entity.response.GetAgencyRespVO;
import com.liepin.worklog_agency.mapper.AgencyMapper;
import com.liepin.worklog_agency.service.AgencyService;
import io.swagger.models.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AgencyServiceImpl extends ServiceImpl<AgencyMapper,Agency> implements AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public Result<GetAgencyRespVO> getAgencyList(GetAgencyReqVO reqVO) {

        GetAgencyRespVO respVO = new GetAgencyRespVO();
        if("ALL".equals(reqVO.getAuditStatus())){
            reqVO.setAuditStatus("");
        }
        List<Agency> agenctList = agencyMapper.getAgenctList(reqVO);
        List<AgencyBrief> agencyBriefList = new ArrayList<>();

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId,StpUtil.getLoginIdAsLong()));
        String username = user.getName();

        for(Agency agency:agenctList){
            AgencyBrief agencyBrief = new AgencyBrief();
            BeanUtils.copyProperties(agency,agencyBrief);
            if(agencyBrief.getAuditStatus().equals("WAIT") && agencyBrief.getCreateId().equals(username) ||
            !agencyBrief.getAuditStatus().equals("WAIT"))
            agencyBriefList.add(agencyBrief);
        }
        respVO.setList(agencyBriefList);
        int size = agencyBriefList.size();
        respVO.setTotal((long) size);
        return Result.success(respVO);
    }

    @Override
    public Result<GetAgencyRespVO> getWaitAgencyList(GetAgencyReqVO reqVO) {
        GetAgencyRespVO respVO = new GetAgencyRespVO();
        reqVO.setAuditStatus(ConstantsEnums.AuditStatus.WAIT.getStatus());
        List<Agency> agenctList = agencyMapper.getAgenctList(reqVO);
        List<AgencyBrief> agencyBriefList = new ArrayList<>();
        for(Agency agency:agenctList){
            AgencyBrief agencyBrief = new AgencyBrief();
            BeanUtils.copyProperties(agency,agencyBrief);
            agencyBriefList.add(agencyBrief);
        }
        respVO.setList(agencyBriefList);
        respVO.setTotal(agencyMapper.getAgencyNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<GetAgencyRespVO> getSelfAgencyList() {


        List<Agency> agencyList = agencyMapper.selectList(new LambdaQueryWrapper<Agency>().
                eq(Agency::getCreateId, StpUtil.getLoginIdAsLong()));
        GetAgencyRespVO respVO = new GetAgencyRespVO();
        List<AgencyBrief> agencyBriefList = new ArrayList<>();
        for(Agency agency:agencyList){
            AgencyBrief agencyBrief = new AgencyBrief();
            BeanUtils.copyProperties(agency,agencyBrief);
            agencyBrief.setCreateId(userMapper.selectOne(new LambdaQueryWrapper<User>().
                    eq(User::getId,StpUtil.getLoginIdAsLong())).getName());
            agencyBriefList.add(agencyBrief);
        }
        respVO.setList(agencyBriefList);
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
        Long id = agencyMapper.selectOne(lambdaQueryWrapper).getId();
        System.out.println(id);
        AuditLog.setAuditFail(TableName.AGENCY, Long.valueOf(id));

    }

    @Override
    public void deleteAgency(Long id) {
        Agency agency = getById(id);
        agency.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
        updateById(agency);
    }

    @Override
    public void updateAgency(UpdateAgencyReqVO reqVO) {

        Agency agency = new Agency();
        BeanUtils.copyProperties(reqVO,agency);
        agency.setId(reqVO.getId());
        agency.setCreateTime(TimeUtil.getNowWithMin());
        agency.setAuditStatus(ConstantsEnums.AuditStatus.WAIT.getStatus());
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

    @Override
    public Result<List<AgencyNameAndId>> getAllAgencyAndId() {
        List<AgencyNameAndId> agencyNameAndIdList = new ArrayList<>();
        List<Agency> agencyList = agencyMapper.selectList(new LambdaQueryWrapper<Agency>().eq(Agency::getDlt, ConstantsEnums.YESNOWAIT.NO).eq(Agency::getAuditStatus,ConstantsEnums.AuditStatus.PASS));
        agencyList.forEach(agency ->{
                AgencyNameAndId agencyNameAndId = new AgencyNameAndId();
                BeanUtils.copyProperties(agency,agencyNameAndId);
                agencyNameAndIdList.add(agencyNameAndId);
                }
                );
        return Result.success(agencyNameAndIdList);
    }

    @Override
    public Result<String> getAgencyById(Long id) {
        Agency agency = getById(id);
        return Result.success(agency.getEnterpriseName());
    }

    @Override
    public Result<ImportAgencyResVO> importAgency(MultipartFile file) {
        checkFile(file);

        AgencyImportListener listener = new AgencyImportListener();

        try {
            log.info("准备导入中介");
            EasyExcel.read(file.getInputStream(),Agency.class,listener).sheet().doRead();

        }catch (Exception ie){
            ie.printStackTrace();
            AssertUtils.throwException(ExceptionsEnums.File.IMPORT_FAIL);
        }
        ImportAgencyResVO resVO = new ImportAgencyResVO();
        resVO.setTotal(listener.getDataNum());
        resVO.setMilSeconds(listener.getTime());
        return Result.success(resVO);
    }

    @Override
    public Result<List<String>> insertAgencyImage(List<MultipartFile> fileList) {
        List<String> fileNameList = new ArrayList<>();
        for(MultipartFile file:fileList){
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.indexOf("."));
            fileName = UUID.randomUUID() + suffixName;
            fileNameList.add(fileName);
//TODO 生成链接？放在哪

// String path =

        }
        return Result.success(fileNameList);
    }

    private void checkFile(MultipartFile file){
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(file),ExceptionsEnums.File.EMPTY_FILE);
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        AssertUtils.isFalse("xlsx".equals(type) || "xls".equals(type),ExceptionsEnums.File.TYPE_NOT_ALLOWED);

    }
}
