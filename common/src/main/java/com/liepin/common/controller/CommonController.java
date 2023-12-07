package com.liepin.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.HashResult;
import com.liepin.common.constant.classes.Result;
import com.liepin.common.constant.config.FileConfig;

import com.liepin.common.util.operationLog.entity.OperationLogResp;
import com.liepin.common.util.operationLog.service.OperationLogService;
import com.liepin.common.util.talentBasicConfig.entity.GetTalentBasicConfigResVO;
import com.liepin.common.util.system.GetSystem;
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
    public HashResult uploadFile(MultipartFile file) {
        try
        {
            //类型检查
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            AssertUtils.isFalse(FileConfig.getAllowType().contains(suffix),ExceptionsEnums.File.TYPE_NOT_ALLOWED);
            // 上传文件路径
            String filePath = new String();
            switch (GetSystem.getType().getValue()){
                case 1 : {
                    filePath = FileConfig.getWinPath() + "\\" + suffix + "\\" ;
                    break;
                }
                case 2 : {
                    filePath = FileConfig.getLinuxPath() + "/" + suffix + "/" ;
                    break;
                }
                default: {
                    AssertUtils.throwException(ExceptionsEnums.File.NO_PATH_FOUND);
                }
            }


            // 上传并返回新文件名称
            AssertUtils.isFalse(FileConfig.getAllowType().contains(suffix),ExceptionsEnums.File.TYPE_NOT_ALLOWED);
            String randomFileName = UUID.randomUUID() + suffix;
            //设置文件转存路径
            File FilePath = new File( filePath + randomFileName);
            if (!FilePath.exists()){
                FilePath.getParentFile().mkdir();
            }
            //将临时文件file转存至FilePath路径
            try {
                file.transferTo(FilePath);
            } catch (IOException e){
                e.printStackTrace();
                AssertUtils.throwException(ExceptionsEnums.File.UPLOAD_FAIL);
            }
            String url = FileConfig.getUrl() + randomFileName;
            HashResult result = HashResult.success();
            result.put("url", url);
            result.put("fileName", randomFileName);
            result.put("originalFilename", file.getOriginalFilename());
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            AssertUtils.throwException(ExceptionsEnums.File.UPLOAD_FAIL);
            return HashResult.error();
        }
    }
    @SaCheckLogin
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
