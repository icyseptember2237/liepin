package com.liepin.contract.cache;


import com.alibaba.fastjson.JSONObject;
import com.liepin.contract.entity.vo.list.ContractRequireListVO;
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
        redisTemplate.opsForValue().setIfAbsent("contract.requireId:" + requireId,require,120, TimeUnit.MINUTES);
    }

    public synchronized ContractRequireListVO Get(Long requireId){
        JSONObject object = (JSONObject)redisTemplate.opsForValue().get("contract.requireId:" + requireId);
        if (ObjectUtils.isEmpty(object)){
            return null;
        }

        ContractRequireListVO require = JSONObject.toJavaObject(object,ContractRequireListVO.class);
        return require;
    }

    public synchronized void Remove(Long requireId){
        redisTemplate.delete("contract.requireId:" + requireId);
    }
}
