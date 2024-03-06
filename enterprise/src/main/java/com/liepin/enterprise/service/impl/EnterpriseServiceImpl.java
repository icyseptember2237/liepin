package com.liepin.enterprise.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.config.thread.AsyncExecutor;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.enterprise.constant.EnterprisePrivateStatus;
import com.liepin.enterprise.entity.base.EnterpriseInfo;
import com.liepin.enterprise.entity.base.EnterprisePrivate;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.AlterEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.req.EnterpriseListVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseInfoRespVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListVO;
import com.liepin.enterprise.entity.vo.resp.ImportEnterpriseRespVO;
import com.liepin.enterprise.listener.EnterpriseImportListener;
import com.liepin.enterprise.mapper.EnterpriseMapper;
import com.liepin.enterprise.service.EnterpriseService;
import com.liepin.enterprise.service.base.impl.EnterpriseInfoServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterpriseOceanServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterprisePrivateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseInfoServiceImpl enterpriseInfoService;
    private final EnterpriseMapper enterpriseMapper;
    private final EnterpriseOceanServiceImpl enterpriseOceanService;
    private final EnterprisePrivateServiceImpl enterprisePrivateService;
    private final RedisTemplate redisTemplate;


    @Autowired
    public EnterpriseServiceImpl(EnterpriseMapper enterpriseMapper,EnterpriseInfoServiceImpl enterpriseInfoService,
                                 EnterpriseOceanServiceImpl enterpriseOceanService,EnterprisePrivateServiceImpl enterprisePrivateService,
                                 RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        this.enterprisePrivateService = enterprisePrivateService;
        this.enterpriseOceanService = enterpriseOceanService;
        this.enterpriseInfoService = enterpriseInfoService;
        this.enterpriseMapper = enterpriseMapper;
    }

    @Override
    public Result<GetEnterpriseListRespVO> getEnterpriseList(GetEnterpriseListReqVO reqVO){
        GetEnterpriseListRespVO respVO = new GetEnterpriseListRespVO();
        List<GetEnterpriseListVO> list = new ArrayList<>();
        list = enterpriseMapper.getEnterpriseOceanList(reqVO);

        respVO.setList(list);
        respVO.setTotal(enterpriseMapper.getEnterpriseOceanNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<GetEnterpriseInfoRespVO> getEnterpriseInfo(Long id){
        EnterpriseInfo info = enterpriseInfoService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),ExceptionsEnums.Enterprise.NO_DATA);

        GetEnterpriseInfoRespVO respVO = new GetEnterpriseInfoRespVO();
        BeanUtils.copyProperties(info,respVO);
        respVO.setList(enterpriseMapper.getFollowupHistory(id,1,5));
        return Result.success(respVO);
    }

    @Override
    public Result<ImportEnterpriseRespVO> importEnterprise(MultipartFile file){
        // 检查文件
        checkFile(file);

        EnterpriseImportListener listener = new EnterpriseImportListener();
        try {
            log.info("准备导入单位公海");
            EasyExcel.read(file.getInputStream(),EnterpriseInfo.class,listener).sheet().doRead();

        } catch (Exception ie){
            ie.printStackTrace();
            AssertUtils.throwException(ie.getMessage());
        }
        ImportEnterpriseRespVO respVO = new ImportEnterpriseRespVO();
        respVO.setTotal(listener.getDataNum());
        respVO.setMilSeconds(listener.getTime());
        return Result.success(respVO);
    }

    private void checkFile(MultipartFile file){
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(file),ExceptionsEnums.File.EMPTY_FILE);
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        AssertUtils.isFalse("xlsx".equals(type) || "xls".equals(type),ExceptionsEnums.File.TYPE_NOT_ALLOWED);

    }

    @Override
    public Result addEnterprise(AddEnterpriseReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getName()),
                "单位名称不能为空");
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getEmail()) || StringUtils.isNotEmpty(reqVO.getPhone()),
                "联系方式不能为空");

        try {
            EnterpriseInfo info = new EnterpriseInfo();
            BeanUtils.copyProperties(reqVO,info);
            info.setCreateTime(TimeUtil.getNowWithSec());
            enterpriseInfoService.save(info);
        } catch (Exception e){
            AssertUtils.throwException(ExceptionsEnums.Enterprise.INSERT_FAIL);
        }

        return Result.success();
    }

    @Override
    public Result alterEnterprise(AlterEnterpriseReqVO reqVO){
        EnterpriseInfo info = enterpriseInfoService.getById(reqVO.getId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info) && ConstantsEnums.YESNOWAIT.NO.getValue().equals(info.getDlt()),
                ExceptionsEnums.Enterprise.NO_DATA);
        AssertUtils.isFalse(ConstantsEnums.YESNOWAIT.NO.getValue().equals(info.getIsPrivate()),
                "修改失败, 单位位于私海中");

        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getName()),
                "单位名称不能为空");
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getEmail()) || StringUtils.isNotEmpty(reqVO.getPhone()),
                "联系方式不能为空");
        try {
            BeanUtils.copyProperties(reqVO,info);
            enterpriseInfoService.updateById(info);
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            AssertUtils.throwException(ExceptionsEnums.Enterprise.ALTER_FAIL);
        }

        return Result.success();
    }

    @Override
    public Result deleteEnterprise(EnterpriseListVO reqVO){
        AssertUtils.isFalse(!reqVO.getList().isEmpty(),ExceptionsEnums.Common.PARAMTER_IS_ERROR);

        reqVO.getList().forEach(id -> {
            EnterpriseInfo info = enterpriseInfoService.getById(id);
            AssertUtils.isFalse(ConstantsEnums.YESNOWAIT.NO.getValue().equals(info.getIsPrivate()),
                    "删除失败, 单位 " + info.getName() +" 位于私海中");
            info.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
            enterpriseInfoService.updateById(info);
        });

        return Result.success();
    }

    @Override
    public Result pullEnterprise(EnterpriseListVO reqVO){
        AssertUtils.isFalse(!reqVO.getList().isEmpty(),ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        AssertUtils.isFalse(reqVO.getList().size() < 30,ExceptionsEnums.Enterprise.TOO_MANY);
        ArrayList<Long> res = new ArrayList<>();
        for (Long id : reqVO.getList()){
            EnterpriseInfo info = enterpriseInfoService.getById(id);
            if (ObjectUtils.isEmpty(info) || info.getIsPrivate().equals(ConstantsEnums.YESNOWAIT.YES.getValue()))
                continue;

            if (redisTemplate.opsForValue().setIfAbsent("EnterPrise:" + id,StpUtil.getLoginIdAsLong(),2, TimeUnit.SECONDS)){
                HashMap<String,Long> map = new HashMap<>();
                map.put("userId",StpUtil.getLoginIdAsLong());
                map.put("enterPriseId",id);
                redisTemplate.opsForList().leftPush("EnterPriseList",map);
                res.add(id);
                try {
                    JSONObject object = (JSONObject) redisTemplate.opsForList().rightPop("EnterPriseList");
                    if (ObjectUtils.isNotEmpty(object)){
                        map =new HashMap<>();
                        Iterator it =object.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
                            map.put(entry.getKey(), entry.getValue().longValue());
                        }
                        Long userId = map.get("userId");
                        Long enterPriseId = map.get("enterPriseId");
                        info = enterpriseInfoService.getById(enterPriseId);
                        info.setIsPrivate(ConstantsEnums.YESNOWAIT.YES.getValue());
                        enterpriseInfoService.updateById(info);

                        EnterprisePrivate enterprisePrivate = new EnterprisePrivate();
                        enterprisePrivate.setUserId(userId);
                        enterprisePrivate.setInfoId(info.getId());
                        enterprisePrivate.setCreateTime(TimeUtil.getNowWithSec());
                        enterprisePrivate.setStatus(EnterprisePrivateStatus.NOT_CONTACT.getStatus());
                        if (enterprisePrivateService.save(enterprisePrivate))
                            redisTemplate.delete("EnterPrise:" + enterPriseId);
                    }
                } catch (Exception e){
                    log.warn(e.toString());
                }
            } else {
                log.info(id + "冲突");
            }
        }

        return Result.success(res);
    }

}
