package com.dqy.englishstudyapi.util;

import com.dqy.englishstudyapi.helper.RequestDataHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicTableNameUtil {

    public void SetTableName(String tablename,String suffix){
        Map<String,Object> params = new HashMap<>();
        params.put(tablename,suffix);
        RequestDataHelper.setRequestData(params);

    }
}
