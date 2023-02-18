package com.dqy.englishstudyapi.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class JsonUtil<T> {
    final Base64Util base64Util = new Base64Util();
    public T parseJsonStrToJavaObject(String json, Class<T> tClass){
        JSONObject result = JSONObject.parseObject(json);
        return  JSONObject.toJavaObject(result,  tClass);
    }
    public  ArrayList<T> parseJsonStrToArrayList(String json, Class<T> tClass){
        ArrayList<T> result = (ArrayList<T>) JSONObject.parseArray(json,tClass);
        return  result;
    }

    public String parseObjectToJsonString(T t){
        return  JSONObject.toJSONString(t);
    }
    public String parseObjectToJsonStrThenToBase64(T t){
        return base64Util.encodeToString(JSONObject.toJSONString(t));
    }
    public String parseArrayListToJsonStrThenToBase64(ArrayList<T> ts) {
        return   base64Util.encodeToString( JSONArray.toJSONString(ts));
    }
    public ArrayList<T> parseBase64ToJsonStrThenToJavaArrayList(String base64, Class<T> tClass) {
        ArrayList<T> result = (ArrayList<T>) JSONObject.parseArray(base64Util.decodeToString(base64),tClass);
        return  result;
    }

    public T parseBase64ToJsonStrThenToJavaObject(String base64, Class<T> tClass){
        JSONObject result = JSONObject.parseObject(base64Util.decodeToString(base64));
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
