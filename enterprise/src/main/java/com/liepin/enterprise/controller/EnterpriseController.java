package com.liepin.enterprise.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.aspect.ratelimit.RateLimit;
import com.liepin.common.constant.classes.Result;
import com.liepin.enterprise.entity.vo.req.AddEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.AlterEnterpriseReqVO;
import com.liepin.enterprise.entity.vo.req.GetEnterpriseListReqVO;
import com.liepin.enterprise.entity.vo.req.EnterpriseListVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseInfoRespVO;
import com.liepin.enterprise.entity.vo.resp.GetEnterpriseListRespVO;
import com.liepin.enterprise.entity.vo.resp.ImportEnterpriseRespVO;
import com.liepin.enterprise.service.EnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @SaCheckLogin
    @RateLimit(times = 15,withinTime = 60,blockTime = 120)
    public Result<GetEnterpriseListRespVO> getEnterpriseList(@RequestBody GetEnterpriseListReqVO reqVO){
        reqVO.setPageSize(Math.min(15, reqVO.getPageSize()));
        return enterpriseService.getEnterpriseList(reqVO);
    }

    @GetMapping("/getEnterpriseInfo")
    @ApiOperation(value = "查询单位详情")
    @SaCheckLogin
    public Result<GetEnterpriseInfoRespVO> getEnterpriseInfo(@RequestParam Long id){
        return enterpriseService.getEnterpriseInfo(id);
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入单位公海")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<ImportEnterpriseRespVO> importEnterprise(MultipartFile file){
        return enterpriseService.importEnterprise(file);
    }

    @PostMapping("/addEnterprise")
    @ApiOperation(value = "添加单位")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result addEnterprise(@RequestBody AddEnterpriseReqVO reqVO){
        return enterpriseService.addEnterprise(reqVO);
    }

    @PostMapping("/alterEnterprise")
    @ApiOperation(value = "修改单位信息")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result alterEnterprise(@RequestBody AlterEnterpriseReqVO reqVO){
        return enterpriseService.alterEnterprise(reqVO);
    }

    @PostMapping("/deleteEnterprise")
    @ApiOperation(value = "删除单位信息")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result deleteEnterprise(@RequestBody EnterpriseListVO reqVO){
        return enterpriseService.deleteEnterprise(reqVO);
    }

    @PostMapping("/pullEnterprise")
    @ApiOperation(value = "拉入私人单位库")
    @SaCheckRole(value = RoleType.ENTERPRISE.code)
    @RateLimit(times = 15,withinTime = 60,blockTime = 120)
    public Result pullEnterprise(@RequestBody EnterpriseListVO reqVO){
        return enterpriseService.pullEnterprise(reqVO);
    }

}
