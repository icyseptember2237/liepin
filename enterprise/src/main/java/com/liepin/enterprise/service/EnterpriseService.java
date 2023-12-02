package com.liepin.enterprise.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.AlterEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.req.EnterpriseListVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseInfoRespVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import com.liepin.enterprise.entity.vo.resp.ImportEnterpriseRespVO;
import org.springframework.web.multipart.MultipartFile;

public interface EnterpriseService {

    Result<GetEnterpriseListRespVO> getEnterpriseList(GetEnterpriseListReqVO reqVO);

    Result<GetEnterpriseInfoRespVO> getEnterpriseInfo(Long id);

    Result<ImportEnterpriseRespVO> importEnterprise(MultipartFile file);

    Result addEnterprise(AddEnterpriseReqVO reqVO);

    Result alterEnterprise(AlterEnterpriseReqVO reqVO);

    Result deleteEnterprise(EnterpriseListVO reqVO);

    Result pullEnterprise(EnterpriseListVO reqVO);


}
