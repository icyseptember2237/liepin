package com.liepin.common.util.talentBasicConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liepin.common.entity.BasicConfig;
import com.liepin.common.util.talentBasicConfig.entity.CertificationDetail;
import com.liepin.common.util.talentBasicConfig.entity.EntryPair;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TalentBasicConfigMapper extends BaseMapper<BasicConfig>{
    CertificationDetail getCertificateMajor();

    List<String> getProfeesionCertificate();

    List<String> getNineMember();

    List<String> getOtherCertificate();

    List<String> getTechnicianCertificate();

    List<String> getFireEngineer();

    List<String> getSecondConstructor();

    List<String> getFirstConstructor();

    List<String> getPublicDeviceEngineer();

    List<String> getElectricEngineer();

    List<String> getPriceEngineer();

    List<String> getSupervisorEngineer();

    List<String> getSoilWoodEngineer();

    List<String> getConstructEngineer();

    List<String> getRegistryConsoleEngineer();

    List<String> getEvaluator();

    List<String> getConstructor();

    List<EntryPair> getAll();
//    Audit getByTableNameAndId(@Param("tableName") String tableName,@Param("tableId") Long tableId);
}
