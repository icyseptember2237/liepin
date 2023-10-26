package com.liepin.common.config.reqloghandel;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;


public class EnableServletRequestLogImportSelector implements ImportSelector {
    public EnableServletRequestLogImportSelector() {
    }

    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{ServletRequestLogConfiguration.class.getName()};
    }
}
