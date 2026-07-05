package com.dqy.englishstudyapi.util;

import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.BaiduTransError;
import com.dqy.englishstudyapi.entity.BaiduTransSuccess;
import com.dqy.englishstudyapi.entity.frontEntity.OrderFull.Logistics;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Component
public class LogisticsUtil {
    @Value("${dqy.aliyunLogisticsUrl}")
    String url;
    String appcode = "54b84e1dafc64c70a62126bebe2fbfe7";
    @Autowired
    HttpClientUtil httpClientUtil;
    @Autowired
    JsonUtil jsonUtil;
    public ReturnVO get(String traceid,String tracetype){
        ReturnVO returnVO = new ReturnVO();
        if (traceid==null||traceid.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("请求错误");
           return  returnVO;
        }
        String urlSend = url + "?no=" + traceid ;

        if (tracetype!=null&&!tracetype.equals("")){
           urlSend+=("&type="+tracetype);
        }

        ReturnVO returnVO1 =  httpClientUtil.clientLogistics(urlSend,appcode);
        if(returnVO1.getCode()==200){
            String json = (String) returnVO1.getData();
            Logistics logistics = (Logistics) jsonUtil.parseJsonStrToJavaObject2(json, Logistics.class);
            if (logistics !=null&&logistics.getStatus().equals("0")){

                returnVO.setCode(200);
                returnVO.setMessage("请求成功");
                returnVO.setData(logistics);

            }else{
                returnVO.setCode(520);
                returnVO.setMessage(returnVO1.getMessage());

            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("请求错误");
        }
        return  returnVO;
    }
}
