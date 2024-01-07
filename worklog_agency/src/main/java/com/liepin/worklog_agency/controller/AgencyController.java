package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.aspect.operationAspect.OperationAspect;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.enums.OperationModule;
import com.liepin.common.constant.enums.OperationType;
import com.liepin.worklog_agency.entity.base.AddAgencyReqVO;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.entity.base.AgencyNameAndId;
import com.liepin.worklog_agency.entity.base.ImportAgencyResVO;
import com.liepin.worklog_agency.entity.request.GetAgencyReqVO;
import com.liepin.worklog_agency.entity.request.UpdateAgencyReqVO;
import com.liepin.worklog_agency.entity.response.GetAgencyRespVO;
import com.liepin.worklog_agency.service.AgencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@Api(tags = "中介")
public class AgencyController {
    @Autowired
    private AgencyService agencyService;

    @SaCheckLogin
    @PostMapping("/getAgencyList")
    @ApiOperation(value = "获取中介List")
    @OperationAspect(detail = "获取中介List",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
    public Result<GetAgencyRespVO> getAgencyList(@RequestBody GetAgencyReqVO reqVO){
        return agencyService.getAgencyList(reqVO);
    }
    @SaCheckRole
    @GetMapping("/get")
    @ApiOperation(value = "管理员获取未审核的中介List")
    @OperationAspect(detail = "管理员获取未审核",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
    public Result<GetAgencyRespVO> getWaitedAgency(){
        GetAgencyReqVO reqVO = new GetAgencyReqVO();
        return agencyService.getWaitAgencyList(reqVO);
    }
    @SaCheckLogin
    @PostMapping("/post")
    @ApiOperation(value = "添加中介")
    @OperationAspect(detail = "添加中介",type = OperationType.INSERT.value ,module = OperationModule.AGENCY.value)
    public Result addAgency(@RequestBody AddAgencyReqVO reqVO){
        agencyService.addAgency(reqVO);
        return Result.success("插入成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/delete")
    @ApiOperation(value = "删除中介")
    @OperationAspect(detail = "删除中介",type = OperationType.DELETE.value ,module = OperationModule.AGENCY.value)
    public Result deleteAgency(@RequestParam Long id){
        agencyService.deleteAgency(id);
        return Result.success("删除成功");
    }
    @SaCheckLogin
    @PostMapping("/update")
    @ApiOperation(value = "更改中介")
    @OperationAspect(detail = "更改中介",type = OperationType.UPDATE.value ,module = OperationModule.AGENCY.value)
    public Result updateAgency(@RequestBody UpdateAgencyReqVO reqVO){
        agencyService.updateAgency(reqVO);
        return Result.success("更改成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/postPassedAgency")
    @ApiOperation(value = "提交审核的中介(传入的list里面每个agency的audit值需要更改)")
    @OperationAspect(detail = "提交审核中介",type = OperationType.UPDATE.value ,module = OperationModule.AGENCY.value)
    public Result postPassedAgency(@RequestBody List<Agency> agencyList){
        agencyService.updateUnpassedAgency(agencyList);
        return Result.success("提交成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/rejectAgency")
    @ApiOperation(value = "拒绝待审核中介")
    @OperationAspect(detail = "拒绝待审核中介",type = OperationType.UPDATE.value ,module = OperationModule.AGENCY.value)
    public Result rejectAgency(@RequestParam Long id){
            return agencyService.rejectAgency(id);
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/passAgency")
    @ApiOperation(value = "通过待审核中介")
    @OperationAspect(detail = "通过待审核中介",type = OperationType.UPDATE.value ,module = OperationModule.AGENCY.value)
    public Result passAgency(@RequestParam Long id){
        return agencyService.passAgency(id);
    }

    @SaCheckLogin
    @GetMapping("/getSelfAgency")
    @ApiOperation(value = "查看自己上传的中介")
    @OperationAspect(detail = "查看自己上传的中介",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
    public Result<GetAgencyRespVO> getSelfAgencyList(){
        return agencyService.getSelfAgencyList();
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/getAllAgencyAndId")
    @ApiOperation(value = "管理员获取所有中介和对应id")
    @OperationAspect(detail = "管理员获取所有中介和对应id",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
    public Result<List<AgencyNameAndId>> getAllAgencyAndId(){
        return agencyService.getAllAgencyAndId();
    }

    @SaCheckRole
    @GetMapping("/getDetailAgencyById")
    @ApiOperation(value = "管理员通过id得到中介名称")
    @OperationAspect(detail = "管理员通过id得到中介名称",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
    public Result<String>  getAgencyNameById(@RequestParam Long id){
        return agencyService.getAgencyById(id);
    }

    @SaCheckLogin
    @PostMapping("/import")
    @ApiOperation(value = "批量通过excel导入中介")
    @OperationAspect(detail = "批量通过excel导入中介",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
    public Result<ImportAgencyResVO> importAgency(MultipartFile file){
        return agencyService.importAgency(file);
    }

//    @SaCheckLogin
//    @PostMapping
//    @ApiOperation(value = "上传中介认证图片")
//    @OperationAspect(detail = "上传中介认证图片",type = OperationType.SERACH.value ,module = OperationModule.AGENCY.value)
//    public Result<List<String>> insertAgencyImage(@RequestParam("fileList") List<MultipartFile> fileList){
//        return agencyService.insertAgencyImage(fileList);
//    }
}
