package com.liepin.talent.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.talent.constant.TalentPrivateStatus;
import com.liepin.talent.entity.base.TalentInfo;
import com.liepin.talent.entity.base.TalentPrivate;
import com.liepin.talent.entity.vo.req.AddTalentReqVO;
import com.liepin.talent.entity.vo.req.AlterTalentReqVO;
import com.liepin.talent.entity.vo.req.GetTalentListReqVO;
import com.liepin.talent.entity.vo.req.PullTalentReqVO;
import com.liepin.talent.entity.vo.resp.GetTalentInfoRespVO;
import com.liepin.talent.entity.vo.resp.GetTalentListRespVO;
import com.liepin.talent.entity.vo.resp.ImportTalentRespVO;
import com.liepin.talent.listener.TalentImportListener;
import com.liepin.talent.mapper.TalentMapper;
import com.liepin.talent.service.TalentService;
import com.liepin.talent.service.base.impl.TalentInfoServiceImpl;
import com.liepin.talent.service.base.impl.TalentPrivateServiceImpl;
import com.liepin.worklog_agency.service.Impl.AgencyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TalentServiceImpl implements TalentService {

    private final TalentMapper talentMapper;
    private final TalentInfoServiceImpl talentInfoService;
    private final TalentPrivateServiceImpl talentPrivateService;
    private final RedisTemplate redisTemplate;

    private final AgencyServiceImpl agencyService;

    @Autowired
    public TalentServiceImpl(TalentMapper talentMapper, TalentInfoServiceImpl talentInfoService,
                             TalentPrivateServiceImpl talentPrivateService, RedisTemplate redisTemplate,
                             AgencyServiceImpl agencyService) {
        this.agencyService = agencyService;
        this.redisTemplate = redisTemplate;
        this.talentPrivateService = talentPrivateService;
        this.talentInfoService = talentInfoService;
        this.talentMapper = talentMapper;
    }

    @Override
    public Result<GetTalentListRespVO> getTalentList(GetTalentListReqVO reqVO) {
        GetTalentListRespVO respVO = new GetTalentListRespVO();
        respVO.setList(talentMapper.getTalentList(reqVO));
        respVO.setTotal(talentMapper.getTalentListNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<GetTalentInfoRespVO> getTalentInfo(Long id) {
        TalentInfo info = talentInfoService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info), ExceptionsEnums.Talent.NO_DATA);

        GetTalentInfoRespVO respVO = new GetTalentInfoRespVO();
        BeanUtils.copyProperties(info, respVO);
        if (info.getAgencyId() != 0){
            respVO.setAgencyName(agencyService.getById(info.getAgencyId()).getEnterpriseName());
        }
        respVO.setList(talentMapper.getFollowupHistory(id, 1, 5));
        return Result.success(respVO);
    }

    @Override
    public Result<ImportTalentRespVO> importTalent(MultipartFile file) {
        // 检查文件
        checkFile(file);

        TalentImportListener listener = new TalentImportListener();
        try {
            log.info("准备导入人才公海");
            EasyExcel.read(file.getInputStream(), TalentInfo.class, listener).sheet().doRead();

        } catch (Exception ie) {
            ie.printStackTrace();
            AssertUtils.throwException(ExceptionsEnums.File.IMPORT_FAIL);
        }
        ImportTalentRespVO respVO = new ImportTalentRespVO();
        respVO.setTotal(listener.getDataNum());
        respVO.setMilSeconds(listener.getTime());
        return Result.success(respVO);
    }

    private void checkFile(MultipartFile file) {
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(file), ExceptionsEnums.File.EMPTY_FILE);
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        AssertUtils.isFalse("xlsx".equals(type) || "xls".equals(type), ExceptionsEnums.File.TYPE_NOT_ALLOWED);

    }

    @Override
    public Result addTalent(AddTalentReqVO reqVO) {
        TalentInfo info = new TalentInfo();
        BeanUtils.copyProperties(reqVO, info);
        info.setCreateTime(TimeUtil.getNowWithSec());
        talentInfoService.save(info);
        return Result.success();
    }

    @Override
    public Result alterTalent(AlterTalentReqVO reqVO) {
        TalentInfo info = talentInfoService.getById(reqVO.getId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info)
                        && ConstantsEnums.YESNOWAIT.NO.getValue().equals(info.getIsPrivate())
                        && ConstantsEnums.YESNOWAIT.NO.getValue().equals(info.getDlt()),
                ExceptionsEnums.Talent.ALTER_FAIL);

        BeanUtils.copyProperties(reqVO, info);
        talentInfoService.updateById(info);
        return Result.success();
    }

    @Override
    public Result deleteTalent(Long id) {
        TalentInfo info = talentInfoService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info), ExceptionsEnums.Talent.NO_DATA);
        AssertUtils.isFalse(ConstantsEnums.YESNOWAIT.NO.getValue().equals(info.getIsPrivate()),
                "删除失败, 人才位于私库中");

        info.setDlt(ConstantsEnums.YESNOWAIT.YES.getValue());
        talentInfoService.updateById(info);
        return Result.success();
    }

    @Override

    public Result pullTalent(PullTalentReqVO reqVO) {
        AssertUtils.isFalse(!reqVO.getList().isEmpty(), ExceptionsEnums.Common.PARAMTER_IS_ERROR);
        ArrayList<Long> res = new ArrayList<>();
        for (Long id : reqVO.getList()) {
            TalentInfo info = talentInfoService.getById(id);
            if (ObjectUtils.isEmpty(info) || info.getIsPrivate().equals(ConstantsEnums.YESNOWAIT.YES.getValue()))
                continue;

            if (redisTemplate.opsForValue().setIfAbsent("Talent:" + id, StpUtil.getLoginIdAsLong(), 2, TimeUnit.SECONDS)) {
                HashMap<String, Long> map = new HashMap<>();
                map.put("userId", StpUtil.getLoginIdAsLong());
                map.put("TalentId", id);
                redisTemplate.opsForList().leftPush("TalentList", map);
                res.add(id);
                try {
                    JSONObject object = (JSONObject) redisTemplate.opsForList().rightPop("TalentList");
                    log.info(object.toString());
                    if (ObjectUtils.isNotEmpty(object)) {
                        map = new HashMap<>();
                        Iterator it = object.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
                            map.put(entry.getKey(), entry.getValue().longValue());
                        }
                        Long userId = map.get("userId");
                        Long talentId = map.get("TalentId");
                        info = talentInfoService.getById(talentId);
                        info.setIsPrivate(ConstantsEnums.YESNOWAIT.YES.getValue());
                        talentInfoService.updateById(info);

                        TalentPrivate talentPrivate = new TalentPrivate();
                        talentPrivate.setUserId(userId);
                        talentPrivate.setInfoId(info.getId());
                        talentPrivate.setCreateTime(TimeUtil.getNowWithSec());
                        talentPrivate.setStatus(TalentPrivateStatus.NOT_CONTACT.getStatus());
                        if (talentPrivateService.save(talentPrivate))
                            redisTemplate.delete("Talent:" + talentId);
                    }
                } catch (Exception e) {
                    log.warn(e.toString());
                }
            } else {
                log.info(id + "冲突");
            }
        }

        return Result.success(res);
    }
}
