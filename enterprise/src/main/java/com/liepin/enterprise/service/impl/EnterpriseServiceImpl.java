package com.liepin.enterprise.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.ConstantsEnums;
import com.liepin.common.util.auditlog.AuditLog;
import com.liepin.common.util.auditlog.constant.TableName;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.enterprise.constant.PrivateStatus;
import com.liepin.enterprise.entity.base.EnterpriseInfo;
import com.liepin.enterprise.entity.base.EnterpriseOcean;
import com.liepin.enterprise.entity.base.EnterprisePrivate;
import com.liepin.enterprise.entity.dto.GetEnterpriseListDTO;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListVO;
import com.liepin.enterprise.mapper.EnterpriseMapper;
import com.liepin.enterprise.service.EnterpriseService;
import com.liepin.enterprise.service.base.impl.EnterpriseInfoServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterpriseOceanServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterprisePrivateServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseInfoServiceImpl enterpriseInfoService;
    private final EnterpriseMapper enterpriseMapper;
    private final EnterpriseOceanServiceImpl enterpriseOceanService;
    private final EnterprisePrivateServiceImpl enterprisePrivateService;

    @Autowired
    public EnterpriseServiceImpl(EnterpriseMapper enterpriseMapper,EnterpriseInfoServiceImpl enterpriseInfoService,
                                 EnterpriseOceanServiceImpl enterpriseOceanService,EnterprisePrivateServiceImpl enterprisePrivateService){
        this.enterprisePrivateService = enterprisePrivateService;
        this.enterpriseOceanService = enterpriseOceanService;
        this.enterpriseInfoService = enterpriseInfoService;
        this.enterpriseMapper = enterpriseMapper;
    }

    @Override
    public Result<GetEnterpriseListRespVO> getEnterpriseList(GetEnterpriseListReqVO reqVO){
        GetEnterpriseListRespVO respVO = new GetEnterpriseListRespVO();
        List<GetEnterpriseListDTO> dtos = new ArrayList<>();
        List<GetEnterpriseListVO> list = new ArrayList<>();
        dtos = enterpriseMapper.getEnterpriseOceanList(reqVO);

        if (dtos.get(0).getNum() == 0){
            respVO.setList(new ArrayList<>());
            respVO.setTotal(0l);
            return Result.success(respVO);
        }

        dtos.forEach((dto) -> {
            GetEnterpriseListVO vo = new GetEnterpriseListVO();
            BeanUtils.copyProperties(dto,vo);
            list.add(vo);
        });
        respVO.setList(list);
        respVO.setTotal(dtos.get(0).getNum());
        return Result.success(respVO);
    }

    @Override
    @Transactional
    public Result addEnterprise(AddEnterpriseReqVO reqVO){
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getName()),
                "单位名称不能为空");
        AssertUtils.isFalse(StringUtils.isNotEmpty(reqVO.getEmail()) || StringUtils.isNotEmpty(reqVO.getPhone()),
                "联系方式不能为空");

        try {
            EnterpriseInfo info = new EnterpriseInfo();
            BeanUtils.copyProperties(reqVO,info);
            enterpriseInfoService.save(info);

            EnterpriseOcean ocean = new EnterpriseOcean();
            ocean.setInfoId(info.getId());
            ocean.setCreateTime(TimeUtil.getNowWithSec());
            enterpriseOceanService.save(ocean);
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            AssertUtils.throwException(ExceptionsEnums.Enterprise.INSERT_FAIL);
        }

        return Result.success();
    }

    @Override
    @Transactional
    public Result pullEnterprise(Long id){
        EnterpriseOcean ocean = enterpriseOceanService.getById(id);
        AssertUtils.isFalse(ObjectUtils.isNotEmpty(ocean),
                ExceptionsEnums.Enterprise.NO_DATA);

        try {
            ocean.setIsPrivate(ConstantsEnums.YESNO.YES.getValue());
            enterpriseOceanService.updateById(ocean);

            EnterprisePrivate enterprisePrivate = new EnterprisePrivate();
            enterprisePrivate.setUserId(StpUtil.getLoginIdAsLong());
            enterprisePrivate.setInfoId(ocean.getInfoId());
            enterprisePrivate.setFollowUpId(StpUtil.getLoginIdAsLong());
            enterprisePrivate.setCreateTime(TimeUtil.getNowWithSec());
            enterprisePrivate.setStatus(PrivateStatus.NOT_CONTACT.getStatus());
            enterprisePrivateService.save(enterprisePrivate);
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            AssertUtils.throwException("拉入失败");
        }
        return Result.success();
    }
}
