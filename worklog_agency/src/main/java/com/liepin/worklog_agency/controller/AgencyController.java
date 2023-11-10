package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.AddAgencyReqVO;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.entity.base.AgencyNameAndId;
import com.liepin.worklog_agency.entity.base.ImportAgencyResVO;
import com.liepin.worklog_agency.entity.request.GetAgencyReqVO;
import com.liepin.worklog_agency.entity.request.UpdateAgencyReqVO;
import com.liepin.worklog_agency.entity.response.GetAgencyRespVO;
import com.liepin.worklog_agency.service.AgencyService;
import io.swagger.annotations.Api;
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
    public Result<GetAgencyRespVO> getAgencyList(@RequestBody GetAgencyReqVO reqVO){
        return agencyService.getAgencyList(reqVO);
    }
    @SaCheckRole
    @GetMapping("/get")
    @ApiOperation(value = "管理员获取未审核的中介List")
    public Result<GetAgencyRespVO> getWaitedAgency(){
        GetAgencyReqVO reqVO = new GetAgencyReqVO();
        return agencyService.getWaitAgencyList(reqVO);
    }
    @PostMapping("/post")
    @ApiOperation(value = "添加中介")
    public Result addAgency(@RequestBody AddAgencyReqVO reqVO){
        agencyService.addAgency(reqVO);
        return Result.success("插入成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/delete")
    @ApiOperation(value = "删除中介")
    public Result deleteAgency(@RequestParam Long id){
        agencyService.deleteAgency(id);
        return Result.success("删除成功");
    }
    @SaCheckLogin
    @PostMapping("/update")
    @ApiOperation(value = "更改中介")
    public Result updateAgency(@RequestBody UpdateAgencyReqVO reqVO){
        agencyService.updateAgency(reqVO);
        return Result.success("更改成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/postPassedAgency")
    @ApiOperation(value = "提交审核的中介(传入的list里面每个agency的audit值需要更改)")
    public Result postPassedAgency(@RequestBody List<Agency> agencyList){
        agencyService.updateUnpassedAgency(agencyList);
        return Result.success("提交成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/rejectAgency")
    @ApiOperation(value = "拒绝待审核中介")
    public Result rejectAgency(@RequestParam Long id){
            return agencyService.rejectAgency(id);
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/passAgency")
    @ApiOperation(value = "通过待审核中介")
    public Result passAgency(@RequestParam Long id){
        return agencyService.passAgency(id);
    }

    @SaCheckLogin
    @GetMapping("/getSelfAgency")
    @ApiOperation(value = "查看自己上传的中介")
    public Result<GetAgencyRespVO> getSelfAgencyList(){
        return agencyService.getSelfAgencyList();
    }

    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/getAllAgencyAndId")
    @ApiOperation(value = "管理员获取所有中介和对应id")
    public Result<List<AgencyNameAndId>> getAllAgencyAndId(){
        return agencyService.getAllAgencyAndId();
    }
    @SaCheckRole
    @GetMapping("/getDetailAgencyById")
    @ApiOperation(value = "管理员通过id得到中介名称")
    public Result<String>  getAgencyNameById(@RequestParam Long id){
        return agencyService.getAgencyById(id);
    }
    @SaCheckLogin
    @PostMapping("/import")
    @ApiOperation(value = "批量通过excel导入中介")
    public Result<ImportAgencyResVO> importAgency(MultipartFile file){
        return agencyService.importAgency(file);
    }

}
