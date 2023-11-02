package com.liepin;

import com.alibaba.excel.EasyExcel;
import com.liepin.common.constant.config.FileConfig;
import com.liepin.common.util.system.GetSystem;
import com.liepin.enterprise.entity.base.EnterpriseInfo;
import com.liepin.enterprise.listener.EnterpriseImportListener;
import com.liepin.enterprise.service.base.impl.EnterpriseInfoServiceImpl;
import com.liepin.enterprise.service.base.impl.EnterpriseOceanServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class LiepinApplicationTest {

    @Autowired
    private EnterpriseInfoServiceImpl enterpriseInfoService;
    @Autowired
    private EnterpriseOceanServiceImpl enterpriseOceanService;

    @Test
    void test(){
//        EnterpriseImportListener listener = new EnterpriseImportListener(enterpriseInfoService,enterpriseOceanService);
//        File file = new File("C:\\Users\\September22\\Desktop\\采集结果 .xlsx");
//        EasyExcel.read(file, EnterpriseInfo.class, listener).sheet().doRead();
        //List<EnterpriseInfo> list = enterpriseInfoService.list();
        System.out.println(addressResolution("四川省成都市锦江区工业园区锦华路三段88号汇融广场（锦华）F座5层"));

    }

    public static Map<String,String> addressResolution(String address){
        String regex="((?<province>[^省]+省|.+自治区)|上海|北京|天津|重庆)(?<city>[^市]+市|.+自治州)(?<county>[^县]+县|.+区|.+镇|.+局)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m= Pattern.compile(regex).matcher(address);
        String province=null,city=null,county=null,town=null,village=null;
        Map<String,String> row=null;
        while(m.find()){
            row=new LinkedHashMap<String,String>();
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
