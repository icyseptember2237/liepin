package com.liepin.contract.cache;


import com.alibaba.fastjson.JSONObject;
import com.liepin.contract.entity.vo.list.ContractRequireListVO;
import com.liepin.contract.entity.vo.respvo.GetContractInfoRespVO;
import org.apache.commons.collections4.Get;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RequireCache {

    @Autowired
    private RedisTemplate redisTemplate;

    public synchronized void Set(Long requireId, ContractRequireListVO require){
        redisTemplate.opsForValue().setIfAbsent("contract:requireCache:requireId:" + requireId,require,2880, TimeUnit.MINUTES);
    }

    public synchronized void SetContract(Long contractId, GetContractInfoRespVO contract){
        redisTemplate.opsForValue().setIfAbsent("contract:contractCache:contractId:" + contractId,contract,2880, TimeUnit.MINUTES);
    }

    public synchronized ContractRequireListVO Get(Long requireId){
        JSONObject object = (JSONObject)redisTemplate.opsForValue().get("contract:requireCache:requireId:" + requireId);
        if (ObjectUtils.isEmpty(object)){
            return null;
        }

        ContractRequireListVO require = JSONObject.toJavaObject(object,ContractRequireListVO.class);
        return require;
    }

    public synchronized GetContractInfoRespVO GetContract(Long contractId){
        JSONObject object = (JSONObject)redisTemplate.opsForValue().get("contract:contractCache:contractId:" + contractId);
        if (ObjectUtils.isEmpty(object)){
            return null;
        }

        GetContractInfoRespVO contract = JSONObject.toJavaObject(object,GetContractInfoRespVO.class);
        return contract;
    }

    public synchronized void Remove(Long requireId){
        redisTemplate.delete("contract:requireCache:requireId:" + requireId);
    }

    public synchronized void RemoveContractCache(Long contractId){
        redisTemplate.delete("contract:contractCache:contractId:" + contractId);
    }
}
