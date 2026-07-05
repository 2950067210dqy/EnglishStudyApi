package com.dqy.englishstudyapi.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.dqy.englishstudyapi.helper.RequestDataHelper;
import com.dqy.englishstudyapi.service.ReadtypeService;
import com.dqy.englishstudyapi.service.ReadtypesubService;
import com.dqy.englishstudyapi.service.imp.*;

import java.util.Map;

public class TableNameSuffixHandler implements TableNameHandler {//动态表名

    @Override
    public String dynamicTableName(String sql, String tableName) {//获取最终置顶的表名称
        // 获取参数方法

        Map<String, Object> paramMap = RequestDataHelper.getRequestData();
        if (paramMap==null||paramMap.size()==0){
            return tableName;
        }else {
            paramMap.forEach((k, v) -> System.err.println(k + "----" + v));
            if (paramMap.get("readtypesub")!=null){
                String suffix = (String) paramMap.get("readtypesub");

                paramMap.remove("readtypesub");

                return tableName+suffix;
            }else if (paramMap.get("read")!=null){
                String suffix = (String) paramMap.get("read");

                paramMap.remove("read");

                return tableName+suffix;
            }else if (paramMap.get("ciku")!=null){
                String suffix = (String) paramMap.get("ciku");
                paramMap.remove("ciku");
                tableName=tableName.split("_")[0];
                return tableName+suffix;
            }
            else if (paramMap.get("cikuexample")!=null){
                String suffix = (String) paramMap.get("cikuexample");
                paramMap.remove("cikuexample");

                return tableName+suffix;
            }else if (paramMap.get("liju")!=null){
                String suffix = (String) paramMap.get("liju");
                paramMap.remove("liju");

                return tableName+suffix;
            }else if (paramMap.get("word")!=null){
                String suffix = (String) paramMap.get("word");
                paramMap.remove("word");

                return tableName+suffix;
            }else if (paramMap.get("read")!=null){
                String suffix = (String) paramMap.get("read");
                paramMap.remove("read");

                return tableName+suffix;
            }else if (paramMap.get("test")!=null){
                String suffix = (String) paramMap.get("test");

                paramMap.remove("test");
                return tableName+suffix;
            }

        }

        return  tableName;




    }
}
