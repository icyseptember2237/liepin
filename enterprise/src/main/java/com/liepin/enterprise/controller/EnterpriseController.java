package com.liepin.enterprise.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import com.liepin.enterprise.service.EnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "单位公海")
@RequestMapping("/api/enterpriseOcean")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @PostMapping("/getEnterpriseList")
    @ApiOperation(value = "查询公海页面")
    //@SaCheckLogin
    public Result<GetEnterpriseListRespVO> getEnterpriseList(@RequestBody GetEnterpriseListReqVO reqVO){
        return enterpriseService.getEnterpriseList(reqVO);
    }

    @GetMapping("/import")
    @ApiOperation(value = "导入单位公海")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result importEnterprise(MultipartFile file){
        return null;
    }

    @PostMapping("/addEnterprise")
    @ApiOperation(value = "添加单位")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result addEnterprise(@RequestBody AddEnterpriseReqVO reqVO){
        return enterpriseService.addEnterprise(reqVO);
    }

    @GetMapping("/pullEnterprise")
    @ApiOperation(value = "拉人私人单位库")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    public Result pullEnterprise(@Parameter(description = "公海id") @RequestParam Long id){
        return enterpriseService.pullEnterprise(id);
    }

}
