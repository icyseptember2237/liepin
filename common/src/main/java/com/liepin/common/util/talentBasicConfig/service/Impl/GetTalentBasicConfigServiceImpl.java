package com.liepin.common.util.talentBasicConfig.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.entity.BasicConfig;
import com.liepin.common.util.talentBasicConfig.entity.CertificationDetail;
import com.liepin.common.util.talentBasicConfig.entity.EntryPair;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.talentBasicConfig.mapper.TalentBasicConfigMapper;
import com.liepin.common.util.talentBasicConfig.service.GetTalentBasicConfigService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetTalentBasicConfigServiceImpl implements GetTalentBasicConfigService {
    @Autowired
    TalentBasicConfigMapper talentBasicConfigMapper;
    @Override
    public Result<HashMap<String,Object>> getTalentBasicConfig() {
//        GetTalentBasicConfigResVO resVO = new GetTalentBasicConfigResVO();
//        resVO.setProfessionCertificate(talentBasicConfigMapper.getProfeesionCertificate());
//        resVO.setNineMember(talentBasicConfigMapper.getNineMember());
//        resVO.setOtherCertificate(talentBasicConfigMapper.getOtherCertificate());
//        resVO.setTechnicianCertificate(talentBasicConfigMapper.getTechnicianCertificate());
//        resVO.setFireEngineer(talentBasicConfigMapper.getFireEngineer());
//
//        CertificationDetail detail = new CertificationDetail();
//
//        List<EntryPair> list = talentBasicConfigMapper.getAll();
//
//        List<String> secondConstructor = new ArrayList<>();
//        List<String> firstConstructor = new ArrayList<>();
//        List<String> publicDeviceEngineer = new ArrayList<>();
//        List<String> electricEngineer = new ArrayList<>();
//        List<String> priceEngineer = new ArrayList<>();
//        List<String> supervisorEngineer = new ArrayList<>();
//        List<String> soilWoodEngineer = new ArrayList<>();
//        List<String> constructEngineer = new ArrayList<>();
//        List<String> registryConsoleEngineer = new ArrayList<>();
//        List<String> evaluator = new ArrayList<>();
//        List<String> constructor = new ArrayList<>();
//
//
//        for(EntryPair entryPair:list){
//            String id = String.valueOf(entryPair.getId());
//            switch (id){
//
//                case "7":secondConstructor.add(entryPair.getValueName());
//                case "8":firstConstructor.add(entryPair.getValueName());
//                case "9":publicDeviceEngineer.add(entryPair.getValueName());
//                case "10":electricEngineer.add(entryPair.getValueName());
//                case "11":priceEngineer.add(entryPair.getValueName());
//                case "12":supervisorEngineer.add(entryPair.getValueName());
//                case "13":soilWoodEngineer.add(entryPair.getValueName());
//                case "14":constructEngineer.add(entryPair.getValueName());
//                case "15":registryConsoleEngineer.add(entryPair.getValueName());
//                case "16":evaluator.add(entryPair.getValueName());
//                case "17":constructor.add(entryPair.getValueName());
//                break;
//                default:
//            }
//        }
//        detail.setSecondConstructor(secondConstructor);
//        detail.setFirstConstructor(firstConstructor);
//        detail.setPublicDeviceEngineer(publicDeviceEngineer);
//        detail.setElectricEngineer(electricEngineer);
//        detail.setPriceEngineer(priceEngineer);
//        detail.setSupervisorEngineer(supervisorEngineer);
//        detail.setSoilWoodEngineer(soilWoodEngineer);
//        detail.setConstructEngineer(constructEngineer);
//        detail.setRegistryConsoleEngineer(registryConsoleEngineer);
//        detail.setEvaluator(evaluator);
//        detail.setConstructor(constructor);
//
////        detail.setSecondConstructor(talentBasicConfigMapper.getSecondConstructor());
////        detail.setFirstConstructor(talentBasicConfigMapper.getFirstConstructor());
////        detail.setPublicDeviceEngineer(talentBasicConfigMapper.getPublicDeviceEngineer());
////        detail.setElectricEngineer(talentBasicConfigMapper.getElectricEngineer());
////        detail.setPriceEngineer(talentBasicConfigMapper.getPriceEngineer());
////        detail.setSupervisorEngineer(talentBasicConfigMapper.getSupervisorEngineer());
////        detail.setSoilWoodEngineer(talentBasicConfigMapper.getSoilWoodEngineer());
////        detail.setConstructEngineer(talentBasicConfigMapper.getConstructEngineer());
////        detail.setRegistryConsoleEngineer(talentBasicConfigMapper.getRegistryConsoleEngineer());
////        detail.setEvaluator(talentBasicConfigMapper.getEvaluator());
////        detail.setConstructor(talentBasicConfigMapper.getConstructor());
//
//        resVO.setCertificateMajor(detail);
////        CertificationDetail certificateMajor = talentBasicConfigMapper.getCertificateMajor();
////        resVO.setCertificateMajor(certificateMajor);
        HashMap<String,Object> resVO = new HashMap<>();
        List<BasicConfig> configs = talentBasicConfigMapper.selectList(new LambdaQueryWrapper<BasicConfig>()
                .eq(BasicConfig::getDlt,"NO"));
        buildTree(resVO,configs,0L,null);
        resVO.values().removeIf(Objects::isNull);
        System.out.println(resVO);
        return Result.success(resVO);
    }

//    void buildTree(Object res,List<BasicConfig> configs, Long parentId,Object parent) {
//        List<BasicConfig> children = configs.parallelStream()
//                .filter(basicConfig -> basicConfig.getParentId().equals(parentId))
//                .collect(Collectors.toList());
////        System.out.println(configs.size());
////        System.out.println("parentId: "+parentId+" children: "+ children);
//
//        if (children.isEmpty()){
//            if (parent instanceof List)
//                ((List<Object>)parent).add(configs.stream()
//                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
//                    .collect(Collectors.toList())
//                    .get(0)
//                    .getValueName());
//            else{
//                ((HashMap<String,Object>) parent).remove(configs.stream()
//                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
//                    .collect(Collectors.toList())
//                    .get(0)
//                    .getValueName());
//                ((HashMap<String,Object>) parent).put(configs.stream()
//                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
//                    .collect(Collectors.toList())
//                    .get(0)
//                    .getValueName(),null);
//            }
//
//            return;
//        }
//
//        for (BasicConfig config : children){
//            Object temp;
//            if (checkGrandChildEmpty(configs,children)){
//                temp = new ArrayList<Object>();
//            } else {
//                temp = new HashMap<String,Object>();
//            }
//
//            buildTree(temp,configs, config.getId(),res);
//            if (res instanceof HashMap){
//                ((HashMap<String,Object>)res).put(config.getValueName(),temp);
//            } else {
//                ((List<Object>)res).add(temp);
//            }
//
//        }
//    }
//
//    boolean checkGrandChildEmpty(List<BasicConfig> configs,List<BasicConfig> children){
//        for (BasicConfig config : children){
//            if (configs.parallelStream()
//                    .filter(config1 -> config1.getParentId().equals(config.getId()))
//                    .collect(Collectors.toList())
//                    .isEmpty())
//                return true;
//        }
//        return false;
//    }

    void buildTree(HashMap<String,Object> res,List<BasicConfig> configs, Long parentId,HashMap<String,Object> parent) {
        List<BasicConfig> children = configs.parallelStream()
                .filter(basicConfig -> basicConfig.getParentId().equals(parentId))
                .collect(Collectors.toList());

        if (children.isEmpty()){
            parent.remove(configs.stream()
                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
                    .collect(Collectors.toList())
                    .get(0)
                    .getValueName());
            parent.put(configs.stream()
                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
                    .collect(Collectors.toList())
                    .get(0)
                    .getValueName(),null);
            return;
        }

        for (BasicConfig config : children){
            HashMap<String,Object> temp = new HashMap<>();
            buildTree(temp,configs, config.getId(),res);
            if (ObjectUtils.isNotEmpty(config.getValueName()) && !temp.isEmpty())
                res.put(config.getValueName(),temp);
        }
    }

}
