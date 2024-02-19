package com.liepin.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.config.FileConfig;

import com.liepin.common.entity.UploadRespVO;
import com.liepin.common.util.operationLog.entity.OperationLogResp;
import com.liepin.common.util.operationLog.service.OperationLogService;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.talentBasicConfig.service.GetTalentBasicConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private GetTalentBasicConfigService basicConfigService;
    @Autowired
    private OperationLogService operationLogService;
    @PostMapping("/upload")
    @ApiOperation(value = "通用文件上传接口")
    @SaCheckLogin
    public Result<UploadRespVO> uploadFile(MultipartFile file) {
            //类型检查
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            AssertUtils.isFalse(FileConfig.getAllowType().contains(suffix), ExceptionsEnums.File.TYPE_NOT_ALLOWED);
            // 上传文件路径
            UploadRespVO respVO = new UploadRespVO();
            //生成随机文件名防止同名文件覆盖
            String RandomFileName = UUID.randomUUID() + "." + suffix;
            //设置文件转存路径
            File FilePath = new File(FileConfig.getLinuxPath() + suffix + "/" + RandomFileName);
            //将临时文件file转存至FilePath路径
            try {
                file.transferTo(FilePath);
            } catch (IOException e) {
                return Result.fail();
            }
            respVO.setFilename(RandomFileName);
            respVO.setOriginalFilename(file.getOriginalFilename());
            respVO.setSuffix(suffix);
            respVO.setUrl(FileConfig.getUrl() + "file/" + suffix + "/"  + RandomFileName);
            return Result.success(respVO);


    }

    @GetMapping("/getTalentBasicConfig")
    @ApiOperation(value = "获取人才基本配置")
    public Result<GetTalentBasicConfigResVO> getTalentBasicConfig(){
            return basicConfigService.getTalentBasicConfig();
    }

//    @SaCheckRole(value = RoleType.MANAGER.code)
    @SaCheckRole(value = "MANAGER")
    @GetMapping("/getOperationLog")
    @ApiOperation(value = "获取操作日志")
    public Result<OperationLogResp> getOperationLog(){
        return operationLogService.getOperationLog();
    }
}
