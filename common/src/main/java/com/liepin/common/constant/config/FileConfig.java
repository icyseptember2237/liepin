package com.liepin.common.constant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config.file")
@Component
public class FileConfig {
    private static String winPath;
    private static String linuxPath;
    private static String url;
    private static String allowType;

    public void setWinPath(String winPath){
        FileConfig.winPath = winPath;
    }

    public static String getWinPath(){
        return FileConfig.winPath;
    }

    public void setLinuxPath(String linuxPath){
        FileConfig.linuxPath = linuxPath;
    }

    public static String getLinuxPath(){
        return FileConfig.linuxPath;
    }

    public void setUrl(String url){
        FileConfig.url = url;
    }

    public static String getUrl(){
        return FileConfig.url;
    }

    public void setAllowType(String allowType){
        FileConfig.allowType = allowType;
    }

    public static String getAllowType(){
        return FileConfig.allowType;
    }
}
