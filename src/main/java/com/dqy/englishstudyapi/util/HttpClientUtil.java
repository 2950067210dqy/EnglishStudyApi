package com.dqy.englishstudyapi.util;


import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Desc: 远程连接工具类
 */
@Service
public class HttpClientUtil {

    /**
     * 根据远程地址发起访问-参数类型为form表单
     * @param url 远程地址
     * @param method 远程方法
     * @param params  方法参数
     * @return
     */
    public JSONObject client(String url, HttpMethod method, MultiValueMap<String,String> params){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,httpEntity,String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        return jsonObject;
    }

    /**
     * 根据远程地址发起访问-参数类型为JSON
     * @param url 远程地址
     * @param method 远程方法
     * @aram params  方法参数
     * @eturn
     */
    public JSONObject clientJson(String url, HttpMethod method, Map<String,Object> params){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(params);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,httpEntity,String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObjectResult = JSONObject.parseObject(body);
        return jsonObjectResult;
    }
}