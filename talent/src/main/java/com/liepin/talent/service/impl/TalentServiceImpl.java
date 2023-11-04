package com.liepin.talent.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
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
import com.liepin.talent.entity.vo.resp.GetTalentListRespVO;
import com.liepin.talent.entity.vo.resp.ImportTalentRespVO;
import com.liepin.talent.listener.TalentImportListener;
import com.liepin.talent.mapper.TalentMapper;
import com.liepin.talent.service.TalentService;
import com.liepin.talent.service.base.impl.TalentInfoServiceImpl;
import com.liepin.talent.service.base.impl.TalentPrivateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class TalentServiceImpl implements TalentService {

    private final TalentMapper talentMapper;
    private final TalentInfoServiceImpl talentInfoService;
    private final TalentPrivateServiceImpl talentPrivateService;

    @Autowired
    public TalentServiceImpl(TalentMapper talentMapper,TalentInfoServiceImpl talentInfoService,
                             TalentPrivateServiceImpl talentPrivateService){
        this.talentPrivateService = talentPrivateService;
        this.talentInfoService = talentInfoService;
        this.talentMapper = talentMapper;
    }

    @Override
    public Result<GetTalentListRespVO> getTalentList(GetTalentListReqVO reqVO){
        GetTalentListRespVO respVO = new GetTalentListRespVO();
        respVO.setList(talentMapper.getTalentList(reqVO));
        respVO.setTotal(talentMapper.getTalentListNum(reqVO));
        return Result.success(respVO);
    }

    @Override
    public Result<ImportTalentRespVO> importTalent(MultipartFile file){
        // 检查文件
        checkFile(file);

        TalentImportListener listener = new TalentImportListener();
        try {
            log.info("准备导入人才公海");
            EasyExcel.read(file.getInputStream(),TalentInfo.class,listener).sheet().doRead();

        } catch (Exception ie){
            ie.printStackTrace();
            AssertUtils.throwException(ExceptionsEnums.File.IMPORT_FAIL);
        }
        ImportTalentRespVO respVO = new ImportTalentRespVO();
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
    public Result addTalent(AddTalentReqVO reqVO){
        TalentInfo info = new TalentInfo();
        BeanUtils.copyProperties(reqVO,info);
        info.setCreateTime(TimeUtil.getNowWithSec());
        talentInfoService.save(info);
        return Result.success();
    }

    @Override
    public Result alterTalent(AlterTalentReqVO reqVO){
        TalentInfo info = talentInfoService.getById(reqVO.getId());
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),"人才不存在");

        BeanUtils.copyProperties(reqVO,info);
        talentInfoService.updateById(info);
        return Result.success();
    }

    @Override
    public Result deleteTalent(Long id){
        TalentInfo info = talentInfoService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),"人才不存在");

        info.setDlt(ConstantsEnums.YESNO.YES.getValue());
        talentInfoService.updateById(info);
        return Result.success();
    }

    @Override
    @Transactional
    public Result pullTalent(Long id){
        TalentInfo info = talentInfoService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(info),"人才不存在");

        try {
            info.setIsPrivate(ConstantsEnums.YESNO.YES.getValue());
            talentInfoService.updateById(info);

            TalentPrivate talentPrivate = new TalentPrivate();
            talentPrivate.setUserId(StpUtil.getLoginIdAsLong());
            talentPrivate.setInfoId(id);
            talentPrivate.setCreateTime(TimeUtil.getNowWithSec());
            talentPrivate.setStatus(TalentPrivateStatus.NOT_CONTACT.getStatus());
            talentPrivateService.save(talentPrivate);
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            AssertUtils.throwException("拉入失败");
        }

        return Result.success();
    }
}
