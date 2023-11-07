package com.liepin.common.util.talentBasicConfig.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.talentBasicConfig.entity.CertificationDetail;
import com.liepin.common.util.talentBasicConfig.entity.EntryPair;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.talentBasicConfig.mapper.TalentBasicConfigMapper;
import com.liepin.common.util.talentBasicConfig.service.GetTalentBasicConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTalentBasicConfigServiceImpl extends ServiceImpl<TalentBasicConfigMapper, GetTalentBasicConfigResVO> implements GetTalentBasicConfigService {
    @Autowired
    TalentBasicConfigMapper talentBasicConfigMapper;
    @Override
    public Result<GetTalentBasicConfigResVO> getTalentBasicConfig() {
        GetTalentBasicConfigResVO resVO = new GetTalentBasicConfigResVO();
        resVO.setProfessionCertificate(talentBasicConfigMapper.getProfeesionCertificate());
        resVO.setNineMember(talentBasicConfigMapper.getNineMember());
        resVO.setOtherCertificate(talentBasicConfigMapper.getOtherCertificate());
        resVO.setTechnicianCertificate(talentBasicConfigMapper.getTechnicianCertificate());
        resVO.setFireEngineer(talentBasicConfigMapper.getFireEngineer());

        CertificationDetail detail = new CertificationDetail();

//        List<EntryPair> list = talentBasicConfigMapper.getAll();

        detail.setSecondConstructor(talentBasicConfigMapper.getSecondConstructor());
        detail.setFirstConstructor(talentBasicConfigMapper.getFirstConstructor());
        detail.setPublicDeviceEngineer(talentBasicConfigMapper.getPublicDeviceEngineer());
        detail.setElectricEngineer(talentBasicConfigMapper.getElectricEngineer());
        detail.setPriceEngineer(talentBasicConfigMapper.getPriceEngineer());
        detail.setSupervisorEngineer(talentBasicConfigMapper.getSupervisorEngineer());
        detail.setSoilWoodEngineer(talentBasicConfigMapper.getSoilWoodEngineer());
        detail.setConstructEngineer(talentBasicConfigMapper.getConstructEngineer());
        detail.setRegistryConsoleEngineer(talentBasicConfigMapper.getRegistryConsoleEngineer());
        detail.setEvaluator(talentBasicConfigMapper.getEvaluator());
        detail.setConstructor(talentBasicConfigMapper.getConstructor());

        resVO.setCertificateMajor(detail);
//        CertificationDetail certificateMajor = talentBasicConfigMapper.getCertificateMajor();
//        resVO.setCertificateMajor(certificateMajor);
        return Result.success(resVO);
    }
}
