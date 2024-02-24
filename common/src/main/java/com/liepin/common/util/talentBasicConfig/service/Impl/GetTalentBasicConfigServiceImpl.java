package com.liepin.common.util.talentBasicConfig.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.entity.BasicConfig;
import com.liepin.common.util.talentBasicConfig.entity.CertificationDetail;
import com.liepin.common.util.talentBasicConfig.entity.EntryPair;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.talentBasicConfig.entity.Node;
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
    public Result<Node> getTalentBasicConfig() {
//        HashMap<String,Object> resVO = new HashMap<>();
//        List<BasicConfig> configs = talentBasicConfigMapper.selectList(new LambdaQueryWrapper<BasicConfig>()
//                .eq(BasicConfig::getDlt,"NO"));
//        buildTree(resVO,configs,0L,null);
//        resVO.values().removeIf(Objects::isNull);
//        System.out.println(resVO);
//        return Result.success(resVO);

        Node res = new Node();
        res.setValue("基本配置");
        List<BasicConfig> configs = talentBasicConfigMapper.selectList(new LambdaQueryWrapper<BasicConfig>()
                .eq(BasicConfig::getDlt,"NO"));
        buildTree(res,configs,0L);
        return Result.success(res);
    }

//    void buildTree(HashMap<String,Object> res,List<BasicConfig> configs, Long parentId,HashMap<String,Object> parent) {
//        List<BasicConfig> children = configs.parallelStream()
//                .filter(basicConfig -> basicConfig.getParentId().equals(parentId))
//                .collect(Collectors.toList());
//
//        if (children.isEmpty()){
//            parent.remove(configs.stream()
//                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
//                    .collect(Collectors.toList())
//                    .get(0)
//                    .getValueName());
//            parent.put(configs.stream()
//                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
//                    .collect(Collectors.toList())
//                    .get(0)
//                    .getValueName(),null);
//            return;
//        }
//
//        for (BasicConfig config : children){
//            HashMap<String,Object> temp = new HashMap<>();
//            buildTree(temp,configs, config.getId(),res);
//            if (ObjectUtils.isNotEmpty(config.getValueName()) && !temp.isEmpty())
//                res.put(config.getValueName(),temp);
//        }
//    }

    void buildTree(Node res,List<BasicConfig> configs,Long parentId){
        List<BasicConfig> children = configs.parallelStream()
                .filter(basicConfig -> basicConfig.getParentId().equals(parentId))
                .collect(Collectors.toList());

        if (children.isEmpty()){
            res.setValue(configs
                    .parallelStream()
                    .filter(basicConfig -> basicConfig.getId().equals(parentId))
                    .collect(Collectors.toList())
                    .get(0)
                    .getValueName());
            return;
        }

        for (BasicConfig conf : children){
            Node temp = new Node();
            temp.setValue(conf.getValueName());
            buildTree(temp,configs,conf.getId());
            res.getChildren().add(temp);
        }
    }

}
