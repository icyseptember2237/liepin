package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.AddAgencyReqVO;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.entity.base.AgencyNameAndId;
import com.liepin.worklog_agency.entity.base.ImportAgencyResVO;
import com.liepin.worklog_agency.entity.request.GetAgencyReqVO;
import com.liepin.worklog_agency.entity.request.UpdateAgencyReqVO;
import com.liepin.worklog_agency.entity.response.GetAgencyRespVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AgencyService extends IService<Agency> {
    Result<GetAgencyRespVO> getAgencyList(GetAgencyReqVO reqVO);

    Result<GetAgencyRespVO> getWaitAgencyList(GetAgencyReqVO reqVO);

    Result<GetAgencyRespVO> getSelfAgencyList();


    void addAgency(AddAgencyReqVO  reqVO);

    void deleteAgency(Long id);

    void updateAgency(UpdateAgencyReqVO reqVO);


    void updateUnpassedAgency(List<Agency> agencyList);

    Result rejectAgency(Long id);

    Result passAgency(Long id);

    Result<List<AgencyNameAndId>> getAllAgencyAndId();

    Result<String> getAgencyById(Long id);

    Result<ImportAgencyResVO> importAgency(MultipartFile file);
}
