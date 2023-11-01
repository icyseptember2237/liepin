package com.liepin.enterprise.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface EnterpriseService {

    Result<GetEnterpriseListRespVO> getEnterpriseList(GetEnterpriseListReqVO reqVO);


    Result addEnterprise(AddEnterpriseReqVO reqVO);

    Result pullEnterprise(Long id);


}
