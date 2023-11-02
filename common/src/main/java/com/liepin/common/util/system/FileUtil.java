package com.liepin.common.util.system;

import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.config.exception.ExceptionsEnums;
import com.liepin.common.constant.config.FileConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileUtil {
    public static File createFile(String fileName) {
        try {
            switch (GetSystem.getType().getValue()) {
                case 1: {
                    File file = new File(FileConfig.getWinPath() + fileName);
                    log.info(file.getPath());
                    file.createNewFile();
                    return file;
                }
                case 2: {
                    File file = new File(FileConfig.getLinuxPath() + fileName);
                    log.info(file.getPath());
                    file.createNewFile();
                    return file;
                }
                default: {
                    AssertUtils.throwException(ExceptionsEnums.File.NO_PATH_FOUND);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            AssertUtils.throwException("创建文件失败");
        }
        return null;
    }
}
