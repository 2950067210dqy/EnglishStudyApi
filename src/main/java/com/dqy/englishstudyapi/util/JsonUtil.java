package com.dqy.englishstudyapi.util;

import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.frontEntity.CikuWord;
import com.dqy.englishstudyapi.tablebean.Word;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class JsonUtil<T> {
    public T parseJsonToJavaObject(String json, Class<T> tClass){
        JSONObject result = JSONObject.parseObject(json);
        return  JSONObject.toJavaObject(result,  tClass);
    }
    public void storeJson(String jsonStr,File file){
        FileWriter fileWriter =null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public  String readJsonFile(String filePath) {
        String jsonStr = "";
        try {
            File jsonFile = new File(filePath);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public  String readJsonFile(File file) {
        String jsonStr = "";
        try {
            Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
