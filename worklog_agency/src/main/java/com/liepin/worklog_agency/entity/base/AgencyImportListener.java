package com.liepin.worklog_agency.entity.base;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.liepin.common.config.exception.AssertUtils;
import com.liepin.common.util.system.FileUtil;
import com.liepin.common.util.time.TimeUtil;
import com.liepin.worklog_agency.mapper.AgencyMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.StringJoiner;

@Data
@Slf4j
public class AgencyImportListener extends AnalysisEventListener<Agency> {

    private File importFile;

    private Long dataNum = new Long(0l);

    private Long time = new Long(0l);

    private Long start;

    private FileWriter fileWriter;

    private BufferedWriter bufferedWriter;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 开始计时
        start = System.currentTimeMillis();

        // 获取数据实体的字段列表
        Field[] fields = Agency.class.getDeclaredFields();
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
            importFile = FileUtil.createFile(System.currentTimeMillis() + "ImportTalent.txt");
            fileWriter = new FileWriter(importFile,true);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e){
            AssertUtils.throwException("导入失败(创建文件失败)");
        }

    }

    @Override
    public void invoke(Agency info, AnalysisContext context) {
        writeFile(info);
        dataNum++;
    }

    @Override
    @Transactional
    public void doAfterAllAnalysed(AnalysisContext context){
        try {
            bufferedWriter.close();
            fileWriter.close();

            AgencyMapper mapper = SpringUtil.getBean(AgencyMapper.class);
            mapper.importAgency(importFile.getPath());
            time = System.currentTimeMillis()-start;
            log.info("{}条数据上传成功",dataNum);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            importFile.delete();
        }

    }

    private void writeFile(Agency info){
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(StringUtils.isNotEmpty(info.getEnterpriseName()) ? info.getEnterpriseName() : "");
        joiner.add(StringUtils.isNotEmpty(info.getPerson()) ? info.getPerson() : "");
        joiner.add(StringUtils.isNotEmpty(info.getProvince()) ? info.getProvince() : "");
        joiner.add(StringUtils.isNotEmpty(info.getCity()) ? info.getCity() : "");
        joiner.add(StringUtils.isNotEmpty(info.getDetailAddr()) ? info.getDetailAddr() : "");
        joiner.add(StringUtils.isNotEmpty(info.getTel()) ? info.getTel() : "");

        joiner.add(TimeUtil.getNowWithSec());

        try {
            bufferedWriter.write(joiner.toString() + "\n");
            bufferedWriter.flush();
        } catch (Exception e){
            e.printStackTrace();
            AssertUtils.throwException("导入失败(写入文件失败)");
        }

    }
}