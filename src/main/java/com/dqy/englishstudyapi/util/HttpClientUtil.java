package com.dqy.englishstudyapi.util;


import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFull;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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
    public JSONObject clientHaveHeader(String url, HttpMethod method, MultiValueMap<String,String> params,Map<String,String> header){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        header.keySet().forEach(key -> {
            String value = header.get(key);
            headers.add(key, value);
        });
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,httpEntity,String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        return jsonObject;
    }

    public ReturnVO clientLogistics(String urlSend, String appcode){
        ReturnVO returnVO = new ReturnVO();
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon .setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
                returnVO.setCode(200);
                returnVO.setData(json);
                returnVO.setMessage("获取成功");
                System.out.println("正常请求计费(其他均不计费)");
                System.out.println("获取返回的json:");
                System.out.print(json);

                return returnVO;
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                    returnVO.setMessage("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                    returnVO.setMessage("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                    returnVO.setMessage("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                    returnVO.setMessage("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                    returnVO.setMessage("套餐包次数用完 ");
                } else if (httpCode == 403 && error.equals("Api Market Subscription quota exhausted")) {
                    System.out.println("套餐包次数用完，请续购套餐");
                    returnVO.setMessage("套餐包次数用完，请续购套餐");
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    returnVO.setMessage("参数名错误 或 其他错误");
                    System.out.println(error);
                }
                returnVO.setCode(500);
               return returnVO;

            }

        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
            returnVO.setMessage("URL格式错误");
            returnVO.setCode(500);
            return returnVO;
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
            returnVO.setMessage("URL地址错误");
            returnVO.setCode(500);
            return returnVO;
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
             e.printStackTrace();
            returnVO.setMessage("其他错误");
            returnVO.setCode(500);
            return returnVO;
        }
    }
    private String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
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