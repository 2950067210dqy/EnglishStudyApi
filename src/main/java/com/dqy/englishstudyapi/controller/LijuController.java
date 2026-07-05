package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.entity.frontEntity.FrontLiJu;
import com.dqy.englishstudyapi.service.LijuService;
import com.dqy.englishstudyapi.tablebean.Liju;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-11
 */
@RestController
@RequestMapping("liju")
public class LijuController {

    @Autowired
    LijuService lijuService;
    @Autowired
    JsonUtil jsonUtil;
    ReturnVO returnVO ;
    @RequestMapping(value = "/getByWord",method = RequestMethod.POST)
    public ReturnVO getByWord(@RequestParam("word") String word,@RequestParam(value = "limit",required = false,defaultValue = "5")Integer limit){
        returnVO = new ReturnVO();
        if (word==null||"".equals(word)||limit==null){
            returnVO.setMessage("数据为空");
            returnVO.setCode(500);
            return  returnVO;
        }else{
            String initial =String.valueOf(word.charAt(0)) ;
            Liju liju = lijuService.getByWord(initial,word);
            ArrayList<FrontLiJu> frontLiJus = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(liju.getSentences(), FrontLiJu.class);
            if (limit>0&&frontLiJus.size()>limit){
                frontLiJus =new ArrayList<>(frontLiJus.subList(0,limit)) ;
            }
            liju.setSentences(jsonUtil.parseArrayListToJsonStrThenToBase64(frontLiJus));
            if (liju!=null){
                returnVO.setMessage("查询成功");
                returnVO.setCode(200);
                returnVO.setData(liju);

            }else{
                returnVO.setMessage("暂未找到例句");
                returnVO.setCode(500);

            }
        }

        return  returnVO;
    }
}
