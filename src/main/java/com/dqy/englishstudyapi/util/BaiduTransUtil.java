package com.dqy.englishstudyapi.util;

import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.BaiduTransError;
import com.dqy.englishstudyapi.entity.BaiduTransSuccess;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

@Component
public class BaiduTransUtil {
    private String appId= "20211213001026733";
    private String secret="Cyq64lHARyxVA1mU_3lG";
    private String url="https://fanyi-api.baidu.com/api/trans/vip/translate";
    @Autowired
    HttpClientUtil httpClientUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    RandomUtil randomUtil;
    public ReturnVO getChinese(String english){
        ReturnVO returnVO = new ReturnVO();
        String salt = getSalt();
        String sign = getSign(english,salt);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.set("q",english);
        params.set("from","en");
        params.set("to","zh");
        params.set("appid",appId);
        params.set("salt",salt);
        params.set("sign",sign);
        JSONObject result =  httpClientUtil.client(url, HttpMethod.GET,params);
        if(result.containsKey("error_code")){
            returnVO.setCode(500);
            returnVO.setMessage("请求错误");
            returnVO.setData(result.toJavaObject(BaiduTransError.class));
        }else{
            returnVO.setCode(200);
            returnVO.setMessage("请求成功");
            returnVO.setData(result.toJavaObject(BaiduTransSuccess.class));
        }
       return  returnVO;
    }

    public ReturnVO getEnglish(String chinese){
        ReturnVO returnVO = new ReturnVO();
        String salt = getSalt();
        String sign = getSign(chinese,salt);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.set("q",chinese);
        params.set("from","zh");
        params.set("to","en");
        params.set("appid",appId);
        params.set("salt",salt);
        params.set("sign",sign);
        JSONObject result =  httpClientUtil.client(url, HttpMethod.GET,params);
        if(result.containsKey("error_code")){
            returnVO.setCode(500);
            returnVO.setMessage("请求错误");
            returnVO.setData(result.toJavaObject(BaiduTransError.class));
        }else{
            returnVO.setCode(200);
            returnVO.setMessage("请求成功");
            returnVO.setData(result.toJavaObject(BaiduTransSuccess.class));
        }
        return  returnVO;
    }
    private String getSalt(){
      return   randomUtil.randomAll(8);
    }
    private String getSign(String q,String salt){
        return DigestUtils.md5DigestAsHex((appId+q+salt+secret).getBytes(StandardCharsets.UTF_8)) ;

    }
}
