package com.liepin.common.controller;

import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.classes.HashResult;
import com.liepin.common.constant.config.FileConfig;
import com.liepin.common.util.system.GetSystem;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @PostMapping("/upload")
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
}
