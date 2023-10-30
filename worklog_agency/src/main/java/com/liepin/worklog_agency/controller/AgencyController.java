package com.liepin.worklog_agency.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.worklog_agency.entity.base.Agency;
import com.liepin.worklog_agency.service.AgencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@Api(tags = "中介")
public class AgencyController {
    @Autowired
    private AgencyService agencyService;

    @SaCheckLogin
    @GetMapping("/get")
    @ApiOperation(value = "获取中介List")
        public Result<List<Agency>> getAgency(
                @RequestParam String province,
                @RequestParam String city,
                @RequestParam String enterpriseName){
            return agencyService.getAgency(province,city,enterpriseName);
    }

    @SaCheckLogin
    @PostMapping("/post")
    @ApiOperation(value = "添加中介")
        public Result postAgency(@RequestBody Agency agency){
                agencyService.insertAgency(agency);
                return Result.success("插入成功");
    }
    @SaCheckLogin
    @PostMapping("/delete")
    @ApiOperation(value = "删除中介")
        public Result deleteAgency(@RequestBody List<Agency> agencyList){
                agencyService.deleteAgency(agencyList);
                return Result.success("删除成功");
    }
    @SaCheckLogin
    @PostMapping("/update")
    @ApiOperation(value = "更改中介")
        public Result updateAgency(@RequestBody Agency agency){
                agencyService.updateAgency(agency);
                return Result.success("更改成功");
    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @GetMapping("/getUnpassedAgency")
    @ApiOperation(value = "获取未通过审核中介")
        public Result<List<Agency>> getUnpassedAgency(){
                return agencyService.getUnpassedAgency();

    }
    @SaCheckRole(value = RoleType.MANAGER.code)
    @PostMapping("/postPassedAgency")
    @ApiOperation(value = "提交通过审核的中介")
        public Result postPassedAgency(@RequestBody List<Agency> agencyList){
                agencyService.updateUnpassedAgency(agencyList);
                return Result.success("提交成功");
    }
}
