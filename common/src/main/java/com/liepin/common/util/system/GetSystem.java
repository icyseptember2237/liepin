package com.liepin.common.util.system;

import com.liepin.common.constant.enums.EnumsConstants;
import org.springframework.stereotype.Component;

@Component
public class GetSystem {
    public static EnumsConstants.SystemType getType(){
        if (System.getProperty("os.name").contains("Win"))
            return EnumsConstants.SystemType.WINDOWS;
        else if (System.getProperty("os.name").contains("Linux"))
            return EnumsConstants.SystemType.LINUX;
        else
            return EnumsConstants.SystemType.OTHER;
    }
}
