package com.dqy.englishstudyapi.controller;

import com.dqy.englishstudyapi.entity.BaiduTransSuccess;
import com.dqy.englishstudyapi.helper.RequestDataHelper;
import com.dqy.englishstudyapi.service.ReadtypesubService;
import com.dqy.englishstudyapi.util.BaiduTransUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import kotlin.jvm.internal.MagicApiIntrinsics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("baidu")
public class BaiduController {
    @Autowired
    BaiduTransUtil baiduTransUtil;
    @Autowired
    ReadtypesubService readtypesubService;
    ReturnVO returnVO;
    @PostMapping("/trans")
    public  ReturnVO trans(@RequestParam("word")String word){
       ReturnVO returnVO1 =(ReturnVO) baiduTransUtil.getChinese(word.replace("\n",""));
       if (returnVO1.getCode()==200){
           returnVO = new ReturnVO();
           returnVO.setCode(200);
           returnVO.setMessage("获取成功");
           BaiduTransSuccess baiduTransSuccess = (BaiduTransSuccess)(returnVO1.getData());
           returnVO.setData(baiduTransSuccess.getTrans_result().get(0).getDst());
           return returnVO;
       }else{
           return returnVO1;
       }

    }

    @PostMapping("/transMode")
    public  ReturnVO transMode(@RequestParam("word")String word,@RequestParam(value = "mode",defaultValue = "0")Integer mode){
        if (word==null||word.equals("")||mode==null){
            returnVO = new ReturnVO();
            returnVO.setCode(500);
            returnVO.setMessage("参数为空");
            return returnVO;
        }
        ReturnVO returnVO1 = new ReturnVO();
        if (mode==0){
            returnVO1=(ReturnVO) baiduTransUtil.getEnglish(word.replace("\n",""));
        }else{
            returnVO1=(ReturnVO) baiduTransUtil.getChinese(word.replace("\n",""));
        }

        if (returnVO1.getCode()==200){
            returnVO = new ReturnVO();
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            BaiduTransSuccess baiduTransSuccess = (BaiduTransSuccess)(returnVO1.getData());
            returnVO.setData(baiduTransSuccess.getTrans_result().get(0).getDst());
            return returnVO;
        }else{
            return returnVO1;
        }

    }
}
