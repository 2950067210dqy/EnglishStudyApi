package com.dqy.englishstudyapi.controller;

import com.dqy.englishstudyapi.entity.frontEntity.CikuFull.CikuFull;
import com.dqy.englishstudyapi.entity.frontEntity.CikuFull.CikuTypeFull;
import com.dqy.englishstudyapi.service.CikuService;
import com.dqy.englishstudyapi.service.CikuexampleService;
import com.dqy.englishstudyapi.service.CikutypeService;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Cikutype;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("cikuFull")
public class CiKuFullController {

    @Autowired
    CikuService cikuService;
    @Autowired
    CikutypeService cikutypeService;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    JsonUtil jsonUtil;
    ReturnVO returnVO;
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public ReturnVO getAll(@RequestParam(value = "uid") Integer uid){
        returnVO = new ReturnVO();
        List<CikuTypeFull> cikuTypeFulls = new ArrayList<>();
        List<Cikutype> cikutypes=  cikutypeService.list();
        if (cikutypes.size()!=0){
            for (Cikutype cikutype:cikutypes
            ) {
                CikuTypeFull cikuTypeFull = new CikuTypeFull();
                cikuTypeFull.setCikutype(cikutype);
                List<CikuFull> cikuFulls = new ArrayList<>();
                Integer cikutypeId= cikutype.getId();
                List<Ciku> cikus =null;
                if (cikutypeId == 1) {

                    cikus  = cikuService.listByUid(cikutypeId,uid);
                }else{
                    cikus  = cikuService.list(cikutypeId);
                }



                for (Ciku ciku:cikus
                ) {
                    Integer cikuTypeId;
                    CikuFull cikuFull = new CikuFull();
                    //寻找我的收藏
                    if (cikutypeId==1){
                        Ciku newciku= cikuService.selectById(Integer.valueOf(ciku.getDsc()),Integer.valueOf(ciku.getDscabb()));
                        cikuTypeId= Integer.valueOf(ciku.getDsc());
                        cikuFull.setNewCikuTypeId(cikuTypeId);
                        ciku=newciku;
                    }else{
                        cikuTypeId=cikutypeId;
                    }


                    cikuFull.setCiku(ciku);
                    Integer cikuId =ciku.getId();
                    Integer cikuExampleCount = cikuexampleService.count(cikuTypeId,cikuId);
                    cikuFull.setCount(cikuExampleCount);
                    cikuFulls.add(cikuFull);


                }
                cikuTypeFull.setCikus((ArrayList<CikuFull>) cikuFulls);
                cikuTypeFulls.add(cikuTypeFull);
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取词库成功");
            returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(cikuTypeFulls));
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取cikutype错误");
        }
        return  returnVO;
    }

}
