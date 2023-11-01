package com.liepin.enterprise.listener;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.enterprise.entity.base.EnterpriseInfo;
import com.liepin.enterprise.entity.base.EnterpriseOcean;
import com.liepin.enterprise.service.base.impl.EnterpriseInfoServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterpriseOceanServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.NamedThreadLocal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Slf4j
public class EnterpriseImportListener extends AnalysisEventListener<EnterpriseInfo> {

    private EnterpriseInfoServiceImpl enterpriseInfoService;

    private EnterpriseOceanServiceImpl enterpriseOceanService;

    private Long dataNum = new Long(0);
    private Long time = new Long(0);

    private static final int CACHE_SIZE = 200;

    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    private List<EnterpriseInfo> list = new ArrayList<>(CACHE_SIZE);

    public EnterpriseImportListener(EnterpriseInfoServiceImpl enterpriseInfoService,EnterpriseOceanServiceImpl enterpriseOceanService){
        this.enterpriseOceanService = enterpriseOceanService;
        this.enterpriseInfoService = enterpriseInfoService;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context){
        // 开始计时
        TIME_THREADLOCAL.set(System.currentTimeMillis());

        // 获取数据实体的字段列表
        Field[] fields = EnterpriseInfo.class.getDeclaredFields();
        // 遍历字段进行判断
        for (Field field : fields) {
            // 获取当前字段上的ExcelProperty注解信息
            ExcelProperty fieldAnnotation = field.getAnnotation(ExcelProperty.class);
            // 判断当前字段上是否存在ExcelProperty注解
            if (ObjectUtils.isNotEmpty(fieldAnnotation)) {
                // 存在ExcelProperty注解则根据注解的index索引到表头中获取对应的表头名
                String headName = headMap.get(fieldAnnotation.index());
                // 判断表头是否为空或是否和当前字段设置的表头名不相同
                if (headName.isEmpty() ) {
                    // 如果为空，则抛出异常不再往下执行
                    AssertUtils.throwException("列模板名称不能为空");
                }
                if (!headName.equals(fieldAnnotation.value()[0])){
                    // 如果不匹配，也抛出异常不再往下执行
                    String[] value = fieldAnnotation.value();
                    Integer index = fieldAnnotation.index()+1;
                    AssertUtils.throwException("第" + index + "列模板名称不匹配，应为"+value[0]);
                }
            }
        }
    }

    @Override
    public void invoke(EnterpriseInfo info, AnalysisContext context) {
        log.info("解析数据:" + JSON.toJSONString(info));
        list.add(info);
        dataNum++;
        if (list.size() >= CACHE_SIZE) {
            // 保存数据并清理空间
            saveData(list);
            list = new ArrayList<>(CACHE_SIZE);
            System.gc();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context){
        saveData(list);
        time = System.currentTimeMillis()-TIME_THREADLOCAL.get();
        log.info("{}条数据上传成功",dataNum);
    }

    private void saveData(List<EnterpriseInfo> list){
        list.forEach( (data) -> {
            enterpriseInfoService.save(data);
            EnterpriseOcean ocean = new EnterpriseOcean();
            ocean.setInfoId(data.getId());
            ocean.setCreateTime(TimeUtil.getNowWithSec());
            enterpriseOceanService.save(ocean);
        });
        log.info("保存{}条数据",list.size());
    }



}
