package com.liepin.common.util.system;

import com.liepin.common.constant.enums.ConstantsEnums;
import org.springframework.stereotype.Component;

@Component
public class GetSystem {
    public static ConstantsEnums.SystemType getType(){
        if (System.getProperty("os.name").contains("Win"))
            return ConstantsEnums.SystemType.WINDOWS;
        else if (System.getProperty("os.name").contains("Linux"))
            return ConstantsEnums.SystemType.LINUX;
        else
            return ConstantsEnums.SystemType.OTHER;
    }
}
