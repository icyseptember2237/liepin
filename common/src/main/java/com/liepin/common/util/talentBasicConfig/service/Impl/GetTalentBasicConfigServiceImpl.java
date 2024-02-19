package com.liepin.common.util.talentBasicConfig.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.util.talentBasicConfig.entity.CertificationDetail;
import com.liepin.common.util.talentBasicConfig.entity.EntryPair;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.talentBasicConfig.mapper.TalentBasicConfigMapper;
import com.liepin.common.util.talentBasicConfig.service.GetTalentBasicConfigService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTalentBasicConfigServiceImpl implements GetTalentBasicConfigService {
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

        List<EntryPair> list = talentBasicConfigMapper.getAll();

        List<String> secondConstructor = new ArrayList<>();
        List<String> firstConstructor = new ArrayList<>();
        List<String> publicDeviceEngineer = new ArrayList<>();
        List<String> electricEngineer = new ArrayList<>();
        List<String> priceEngineer = new ArrayList<>();
        List<String> supervisorEngineer = new ArrayList<>();
        List<String> soilWoodEngineer = new ArrayList<>();
        List<String> constructEngineer = new ArrayList<>();
        List<String> registryConsoleEngineer = new ArrayList<>();
        List<String> evaluator = new ArrayList<>();
        List<String> constructor = new ArrayList<>();


        for(EntryPair entryPair:list){
            String id = String.valueOf(entryPair.getId());
            switch (id){

                case "7":secondConstructor.add(entryPair.getValueName());
                case "8":firstConstructor.add(entryPair.getValueName());
                case "9":publicDeviceEngineer.add(entryPair.getValueName());
                case "10":electricEngineer.add(entryPair.getValueName());
                case "11":priceEngineer.add(entryPair.getValueName());
                case "12":supervisorEngineer.add(entryPair.getValueName());
                case "13":soilWoodEngineer.add(entryPair.getValueName());
                case "14":constructEngineer.add(entryPair.getValueName());
                case "15":registryConsoleEngineer.add(entryPair.getValueName());
                case "16":evaluator.add(entryPair.getValueName());
                case "17":constructor.add(entryPair.getValueName());
                break;
                default:
            }
        }
        detail.setSecondConstructor(secondConstructor);
        detail.setFirstConstructor(firstConstructor);
        detail.setPublicDeviceEngineer(publicDeviceEngineer);
        detail.setElectricEngineer(electricEngineer);
        detail.setPriceEngineer(priceEngineer);
        detail.setSupervisorEngineer(supervisorEngineer);
        detail.setSoilWoodEngineer(soilWoodEngineer);
        detail.setConstructEngineer(constructEngineer);
        detail.setRegistryConsoleEngineer(registryConsoleEngineer);
        detail.setEvaluator(evaluator);
        detail.setConstructor(constructor);

//        detail.setSecondConstructor(talentBasicConfigMapper.getSecondConstructor());
//        detail.setFirstConstructor(talentBasicConfigMapper.getFirstConstructor());
//        detail.setPublicDeviceEngineer(talentBasicConfigMapper.getPublicDeviceEngineer());
//        detail.setElectricEngineer(talentBasicConfigMapper.getElectricEngineer());
//        detail.setPriceEngineer(talentBasicConfigMapper.getPriceEngineer());
//        detail.setSupervisorEngineer(talentBasicConfigMapper.getSupervisorEngineer());
//        detail.setSoilWoodEngineer(talentBasicConfigMapper.getSoilWoodEngineer());
//        detail.setConstructEngineer(talentBasicConfigMapper.getConstructEngineer());
//        detail.setRegistryConsoleEngineer(talentBasicConfigMapper.getRegistryConsoleEngineer());
//        detail.setEvaluator(talentBasicConfigMapper.getEvaluator());
//        detail.setConstructor(talentBasicConfigMapper.getConstructor());

        resVO.setCertificateMajor(detail);
//        CertificationDetail certificateMajor = talentBasicConfigMapper.getCertificateMajor();
//        resVO.setCertificateMajor(certificateMajor);
        return Result.success(resVO);
    }
}
