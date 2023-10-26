package com.liepin.common.util.system;

import com.liepin.common.constant.enums.SystemType;
import org.springframework.stereotype.Component;

@Component
public class GetSystem {
    public static SystemType getType(){
        if (System.getProperty("os.name").contains("Win"))
            return SystemType.WINDOWS;
        else if (System.getProperty("os.name").contains("Linux"))
            return SystemType.LINUX;
        else
            return SystemType.OTHER;
    }
}
