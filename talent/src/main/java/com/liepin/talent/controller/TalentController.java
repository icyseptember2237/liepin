package com.liepin.talent.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.auth.constant.RoleType;
import com.liepin.common.constant.classes.Result;
import com.liepin.talent.entity.vo.req.AddTalentReqVO;
import com.liepin.talent.entity.vo.req.AlterTalentReqVO;
import com.liepin.talent.entity.vo.req.GetTalentListReqVO;
import com.liepin.talent.entity.vo.resp.GetTalentInfoRespVO;
import com.liepin.talent.entity.vo.resp.GetTalentListRespVO;
import com.liepin.talent.entity.vo.resp.ImportTalentRespVO;
import com.liepin.talent.service.TalentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "人才公海")
@RequestMapping("/api/talentOcean")
public class TalentController {

    private final TalentService talentService;

    @Autowired
    public TalentController(TalentService talentService){
        this.talentService = talentService;
    }

    @PostMapping("/getTalentList")
    @ApiOperation(value = "获取人才列表")
    @SaCheckLogin
    public Result<GetTalentListRespVO> getTalentList(@RequestBody GetTalentListReqVO reqVO){
        return talentService.getTalentList(reqVO);
    }

    @GetMapping("/getTalentInfo")
    @ApiOperation(value = "查询人才在公海详情")
    @SaCheckLogin
    public Result<GetTalentInfoRespVO> getTalentInfo(@RequestParam Long id){
        return talentService.getTalentInfo(id);
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入人才公海")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result<ImportTalentRespVO> importTalent(MultipartFile file){
        return talentService.importTalent(file);
    }

    @PostMapping("/addTalent")
    @ApiOperation(value = "添加人才")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result addTalent(@RequestBody AddTalentReqVO reqVO){
        return talentService.addTalent(reqVO);
    }

    @PostMapping("/alterTalent")
    @ApiOperation(value = "修改人才信息")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result alterTalent(@RequestBody AlterTalentReqVO reqVO){
        return talentService.alterTalent(reqVO);
    }

    @PostMapping("/deleteTalent")
    @ApiOperation(value = "删除人才信息")
    @SaCheckRole(value = RoleType.MANAGER.code)
    public Result deleteTalent(@RequestParam Long id){
        return talentService.deleteTalent(id);
    }

    @GetMapping("/pullTalent")
    @ApiOperation(value = "拉入私人人才库")
    @SaCheckRole(value = RoleType.TALENT.code)
    public Result pullTalent(@Parameter(description = "公海id") @RequestParam Long id){
        return talentService.pullTalent(id);
    }
}
