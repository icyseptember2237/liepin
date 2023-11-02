package com.liepin.enterprise.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.util.system.FileUtil;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.enterprise.entity.base.EnterpriseInfo;
import com.liepin.enterprise.entity.base.EnterpriseOcean;
import com.liepin.enterprise.mapper.EnterpriseMapper;
import com.liepin.enterprise.service.base.impl.EnterpriseInfoServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterpriseOceanServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
public class EnterpriseImportListener extends AnalysisEventListener<EnterpriseInfo> {

    private File importFile;

    private Long dataNum = new Long(0);

    private Long time = new Long(0);

    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    private FileWriter fileWriter;

    private BufferedWriter bufferedWriter;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
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

        // 表头匹配, 创建文件和流
        try {
            importFile = FileUtil.createFile(System.currentTimeMillis() + "ImportEnterprise.txt");
            fileWriter = new FileWriter(importFile,true);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e){
            AssertUtils.throwException("创建文件失败");
        }

    }

    @Override
    public void invoke(EnterpriseInfo info, AnalysisContext context) {
        writeFile(info);
        dataNum++;
    }

    @Override
    @Transactional
    public void doAfterAllAnalysed(AnalysisContext context){
        try {
            bufferedWriter.close();
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        EnterpriseMapper mapper = SpringUtil.getBean(EnterpriseMapper.class);
        log.info(importFile.getPath());
        mapper.importEnterprise(importFile.getPath());
        mapper.importEnterpriseOcean();
        importFile.delete();
        time = System.currentTimeMillis()-TIME_THREADLOCAL.get();
        log.info("{}条数据上传成功",dataNum);
    }

    private void writeFile(EnterpriseInfo info){
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(info.getName());
        joiner.add(info.getPhone());
        joiner.add(info.getAddress());
        joiner.add(info.getLegalRepresentative());
        Map<String,String> addr = addressResolution(info.getAddress());
        if (!addr.isEmpty()){
            joiner.add(addr.get("province"));
            joiner.add(addr.get("city"));
            joiner.add(addr.get("county"));
        } else{
            joiner.add("");
            joiner.add("");
            joiner.add("");
        }

        try {
            bufferedWriter.write(joiner.toString() + "\n");
            bufferedWriter.flush();
        } catch (Exception e){
            e.printStackTrace();
            AssertUtils.throwException("写入文件失败");
        }

    }

    // 正则匹配地址
    public static Map<String,String> addressResolution(String address){
        String regex="((?<province>[^省]+省|.+自治区)|上海|北京|天津|重庆)(?<city>[^市]+市|.+自治州)(?<county>[^县]+县|.+区|.+镇|.+局)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m= Pattern.compile(regex).matcher(address);
        String province=null,city=null,county=null,town=null,village=null;
        Map<String,String> row=new LinkedHashMap<String,String>();
        while(m.find()){
            province=m.group("province");
            row.put("province", province==null?"":province.trim());
            city=m.group("city");
            row.put("city", city==null?"":city.trim());
            county=m.group("county");
            row.put("county", county==null?"":county.substring(0,county.indexOf("区")+1));
        }
        return row;
    }

}
