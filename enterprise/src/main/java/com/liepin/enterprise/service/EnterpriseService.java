package com.liepin.enterprise.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.AlterEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import com.liepin.enterprise.entity.vo.resp.ImportEnterpriseRespVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface EnterpriseService {

    Result<GetEnterpriseListRespVO> getEnterpriseList(GetEnterpriseListReqVO reqVO);

    Result<ImportEnterpriseRespVO> importEnterprise(MultipartFile file);

    Result addEnterprise(AddEnterpriseReqVO reqVO);

    Result alterEnterprise(AlterEnterpriseReqVO reqVO);

    Result deleteEnterprise(Long id);

    Result pullEnterprise(Long id);


}
