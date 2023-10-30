package com.liepin.worklog_agency.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.Agency;

import java.util.List;

public interface AgencyService extends IService<Agency> {
    Result<List<Agency>> getAgency(String province, String city, String enterpriseName);

    void insertAgency(Agency agency);

    void deleteAgency(List<Agency> agency);

    void updateAgency(Agency agency);

    Result<List<Agency>> getUnpassedAgency();

    void updateUnpassedAgency(List<Agency> agencyList);
}
