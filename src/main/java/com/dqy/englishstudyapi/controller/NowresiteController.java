package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.endEntity.ReviewEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.NowReciteSimple;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.Base64Util;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-13
 */
@RestController
@RequestMapping("nowresite")
public class NowresiteController {
    @Autowired
    NowresiteService nowresiteService;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    ResiteService resiteService;
    @Autowired
    CikuService cikuService;
    @Autowired
    FreshwordService freshwordService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    JsonUtil jsonUtil;
    ReturnVO returnVO;


    @RequestMapping(value = "/getByRecitingSimpleByUid",method = RequestMethod.POST)
    public ReturnVO getByRecitingSimpleByUid(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else {
            HashMap<String,Integer> param = new HashMap<>();
            param.put("uid",uid);
            param.put("isstudy",1);
            QueryWrapper<Nowresite> wrapper = new QueryWrapper<>();
            wrapper.allEq(param);
            Nowresite nowresite = nowresiteService.getOne(wrapper);
            if (nowresite!=null){
                NowReciteSimple nowReciteSimple = new NowReciteSimple();
                //词库
                if (nowresite.getType()==0){
                    Ciku ciku = cikuService.selectById(nowresite.getCikutypeid(),nowresite.getCikuid());
                    if (ciku!=null){
                        nowReciteSimple.setType(nowresite.getType());
                        nowReciteSimple.setDsc(ciku.getDsc());
                        nowReciteSimple.setDscabb(ciku.getDscabb());
                        nowReciteSimple.setCikuId(nowresite.getCikuid());
                        nowReciteSimple.setCikuTypeId(nowresite.getCikutypeid());
                        nowReciteSimple.setUid(uid);
                        nowReciteSimple.setNowreciteid(nowresite.getId());
                        Resite resite = resiteService.getOne(new QueryWrapper<Resite>().eq("nowresiteid",nowresite.getId()));
                        if (resite!=null){
                            String learn =base64Util.decodeToString(resite.getLearn());
                            String review1 =base64Util.decodeToString(resite.getReview1());
                            String review2 =base64Util.decodeToString(resite.getReview2());
                            String review4 =base64Util.decodeToString(resite.getReview4());
                            String review7 =base64Util.decodeToString(resite.getReview7());
                            String review15 =base64Util.decodeToString(resite.getReview15());
                            String over =base64Util.decodeToString(resite.getOver());
                            Integer count=0;
                            String[] learnIds = learn.split(",");
                            nowReciteSimple.setLearnCount(learnIds.length);
                            count+=learnIds.length;
                            Integer reviewCount = 0;
                            Long differDay = timeUtil.differDay(resite.getCreatetime(),timeUtil.getNowLocalDateTime());
                            System.out.println("differDay:"+differDay);
                            if (differDay>=1L&&differDay<2L){
                                if (!("".equals(review1))){
                                    ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                    if (reviewEnds!=null){
                                        for (ReviewEnd reviewEnd:reviewEnds
                                        ) {
                                            Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                            if (differ>=1L){
                                                reviewCount+=1;
                                            }
                                            count++;
                                        }
                                    }
                                }
                            }
                            else if(differDay>=2L&&differDay<4L){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                            }
                            else if(differDay>=4L&&differDay<7L){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }

                            }
                            else if(differDay>=7L&&differDay<15L){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                            }
                            else if(differDay>=15){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review15,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                            }
                            count +=over.split(",").length;
                            nowReciteSimple.setCount(count);
                            nowReciteSimple.setReviewCount(reviewCount);
                            nowReciteSimple.setReciteid(resite.getId());

                            returnVO.setData(nowReciteSimple);
                            returnVO.setMessage("获取成功");
                            returnVO.setCode(200);
                            return returnVO;
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("resite没有此数据");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("词库没有此数据");
                        return returnVO;
                    }
                }else if (nowresite.getType()==1){
                    //生词本
                    Freshword freshword =freshwordService.getById(nowresite.getCikutypeid());
                    if (freshword!=null){
                        nowReciteSimple.setType(nowresite.getType());
                        nowReciteSimple.setDsc("生词本");
                        nowReciteSimple.setDscabb("用户所收藏的生词本");
                        nowReciteSimple.setCikuId(nowresite.getCikuid());
                        nowReciteSimple.setCikuTypeId(nowresite.getCikutypeid());
                        nowReciteSimple.setUid(uid);
                        nowReciteSimple.setNowreciteid(nowresite.getId());
                        Integer count =0;
                        Resite resite = resiteService.getOne(new QueryWrapper<Resite>().eq("nowresiteid",nowresite.getId()));
                        if (resite!=null){
                            String learn =base64Util.decodeToString(resite.getLearn());
                            String review1 =base64Util.decodeToString(resite.getReview1());
                            String review2 =base64Util.decodeToString(resite.getReview2());
                            String review4 =base64Util.decodeToString(resite.getReview4());
                            String review7 =base64Util.decodeToString(resite.getReview7());
                            String review15 =base64Util.decodeToString(resite.getReview15());
                            String over =base64Util.decodeToString(resite.getOver());
                            ArrayList<WordSimpleEnd> learns = jsonUtil.parseJsonStrToArrayList(learn, WordSimpleEnd.class);
                            if (learns!=null){
                                nowReciteSimple.setLearnCount(learns.size());
                            }
                            count+=learns.size();
                            Integer reviewCount = 0;
                            Long differDay = timeUtil.differDay(resite.getCreatetime(),timeUtil.getNowLocalDateTime());
                            System.out.println("differDay:"+differDay);
                            if (differDay>=1L&&differDay<2L){
                                if (!("".equals(review1))){
                                    ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                    if (reviewEnds!=null){
                                        for (ReviewEnd reviewEnd:reviewEnds
                                        ) {
                                            Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                            if (differ>=1L){
                                                reviewCount+=1;
                                            }
                                            count++;
                                        }
                                    }
                                }
                            }
                            else if(differDay>=2L&&differDay<4L){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                            }
                            else if(differDay>=4L&&differDay<7L){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }

                            }
                            else if(differDay>=7L&&differDay<15L){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                            }
                            else if(differDay>=15){
                                ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                                reviewEnds = jsonUtil.parseJsonStrToArrayList(review15,ReviewEnd.class);
                                if (reviewEnds!=null){
                                    for (ReviewEnd reviewEnd:reviewEnds
                                    ) {
                                        Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                        if (differ>=1L){
                                            reviewCount+=1;
                                        }
                                        count++;
                                    }
                                }
                            }
                            ArrayList<WordSimpleEnd> overs = jsonUtil.parseJsonStrToArrayList(over, WordSimpleEnd.class);
                            if (overs!=null){
                                count+=overs.size();

                            }
                            nowReciteSimple.setCount(count);


                            nowReciteSimple.setLearnCount(learns.size());
                            nowReciteSimple.setReviewCount(reviewCount);
                            nowReciteSimple.setReciteid(resite.getId());
                            returnVO.setData(nowReciteSimple);
                            returnVO.setMessage("获取成功");
                            returnVO.setCode(200);
                            return returnVO;
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("resite没有此数据");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("词库没有此数据");
                        return returnVO;
                    }

                }else{
                    //切片
                }
                returnVO.setCode(400);return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("Nowresite没有此数据");
                return returnVO;
            }
        }

    }


    @RequestMapping(value = "/getByRecitingByUid",method = RequestMethod.POST)
    public ReturnVO getByRecitingByUid(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else {
            HashMap<String,Integer> param = new HashMap<>();
            param.put("uid",uid);
            param.put("isstudy",1);
            QueryWrapper<Nowresite> wrapper = new QueryWrapper<>();
            wrapper.allEq(param);
            Nowresite nowresite = nowresiteService.getOne(wrapper);
            if (nowresite!=null){
                returnVO.setCode(200);
                returnVO.setMessage("查询成功");
                returnVO.setData(nowresite);
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("没有此数据");
            }
        }


        return returnVO;

    }

    @RequestMapping(value = "/getByRecitingFreshWord",method = RequestMethod.POST)
    public ReturnVO getByRecitingFreshWord(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else {
            HashMap<String,Integer> param = new HashMap<>();
            param.put("uid",uid);
            param.put("isstudy",1);
            param.put("type",1);
            QueryWrapper<Nowresite> wrapper = new QueryWrapper<>();
            wrapper.allEq(param);
            Nowresite nowresite = nowresiteService.getOne(wrapper);
            if (nowresite!=null){
                returnVO.setCode(200);
                returnVO.setMessage("查询成功");
                returnVO.setData(nowresite);
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("没有此数据");
            }
        }


        return returnVO;

    }
    @RequestMapping(value = "/getByRecitingCiku",method = RequestMethod.POST)
    public ReturnVO getByRecitingCiku(
            @RequestParam("cikutypeid") Integer cikuTypeId
            ,@RequestParam("cikuid")Integer cikuId
            ,@RequestParam("uid")Integer uid){
            returnVO = new ReturnVO();
            if (cikuId==null||cikuTypeId==null||uid==null){
                returnVO.setCode(500);
                returnVO.setMessage("数据为空");
                return returnVO;
            }else {
                HashMap<String,Integer> param = new HashMap<>();
                param.put("cikutypeid",cikuTypeId);
                param.put("cikuid",cikuId);
                param.put("uid",uid);
                param.put("isstudy",1);
                QueryWrapper<Nowresite> wrapper = new QueryWrapper<>();
                wrapper.allEq(param);
                Nowresite nowresite = nowresiteService.getOne(wrapper);
                if (nowresite!=null){
                    returnVO.setCode(200);
                    returnVO.setMessage("查询成功");
                    returnVO.setData(nowresite);
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("没有此数据");
                }
            }


            return returnVO;

    }


    @RequestMapping(value = "/reciteNewBook",method = RequestMethod.POST)
    public ReturnVO reciteNewBook(
            @RequestParam("cikutypeid") Integer cikuTypeId
            ,@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (cikuTypeId==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else {
            HashMap<String,Integer> param = new HashMap<>();
            param.put("cikutypeid",cikuTypeId);
            param.put("type",1);
            param.put("uid",uid);
            QueryWrapper<Nowresite> wrapper = new QueryWrapper<>();
            wrapper.allEq(param);
            Nowresite nowresite = nowresiteService.getOne(wrapper);
            if (nowresite!=null){
                //查找之前正在背的书 将isstudy置为0 并将现在的要背的书isstudy置为1
                HashMap<String,Integer> param2 = new HashMap<>();
                param2.put("isstudy",1);
                param2.put("uid",uid);
                QueryWrapper<Nowresite> wrapper2 = new QueryWrapper<>();
                wrapper2.allEq(param2);
                Nowresite beforenowresite = nowresiteService.getOne(wrapper2);
                if (beforenowresite!=null){
                    beforenowresite.setIsstudy(0);
                    boolean result = nowresiteService.updateById(beforenowresite);
                    if (result){
                        nowresite.setIsstudy(1);
                        boolean result2 = nowresiteService.updateById( nowresite);
                        if (result2){
                            returnVO.setCode(200);
                            returnVO.setMessage("添加成功1");
                            return returnVO;
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新NowResite词库错误");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("更新beforeNowResite词库错误");
                        return returnVO;
                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("查询beforeNowResite词库错误");
                    return returnVO;
                }
            }else{

                //查找之前正在背的书 将isstudy置为0 并将现在的要背的书插入
                HashMap<String,Integer> param2 = new HashMap<>();
                param2.put("isstudy",1);
                param2.put("uid",uid);
                QueryWrapper<Nowresite> wrapper2 = new QueryWrapper<>();
                wrapper2.allEq(param2);
                Nowresite beforenowresite = nowresiteService.getOne(wrapper2);
                if (beforenowresite!=null){
                    beforenowresite.setIsstudy(0);
                    boolean result = nowresiteService.updateById(beforenowresite);
                    if (result){
                        //插入
                        Nowresite storeRecite = new Nowresite();
                        storeRecite.setCikuid(0);
                        storeRecite.setCikutypeid(cikuTypeId);
                        storeRecite.setUid(uid);
                        storeRecite.setType(1);
                        storeRecite.setDeleted(0);
                        storeRecite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                        storeRecite.setIsstudy(1);
                        boolean result2 =nowresiteService.save(storeRecite);
                        if (result2){
                            Freshword freshword = freshwordService.getById(cikuTypeId);
                            if (freshword!=null){
                                String learn=freshword.getWords();
                                Resite resite = new Resite();
                                resite.setNowresiteid(storeRecite.getId());
                                resite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                                resite.setDeleted(0);
                                resite.setLearn(learn);
                                resite.setReview1("");
                                resite.setReview2("");
                                resite.setReview4("");
                                resite.setReview7("");
                                resite.setReview15("");
                                resite.setOver("");
                                boolean result3 =  resiteService.save(resite);
                                if (result3){
                                    returnVO.setCode(200);
                                    returnVO.setMessage("添加成功2");
                                    return returnVO;
                                }else{
                                    returnVO.setCode(500);
                                    returnVO.setMessage("存储resite错误");
                                    return returnVO;
                                }
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("存储freshword错误");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("查询词库错误");
                            return returnVO;
                        }



                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("更新beforeNowResite词库错误2");
                        return returnVO;
                    }
                }else{

                    //插入
                    Nowresite storeRecite = new Nowresite();
                    storeRecite.setCikuid(0);
                    storeRecite.setCikutypeid(cikuTypeId);
                    storeRecite.setUid(uid);
                    storeRecite.setType(1);
                    storeRecite.setDeleted(0);
                    storeRecite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    storeRecite.setIsstudy(1);
                    boolean result2 =nowresiteService.save(storeRecite);
                    if (result2){
                        Freshword freshword = freshwordService.getById(cikuTypeId);
                        if (freshword!=null){
                            String learn=freshword.getWords();
                            Resite resite = new Resite();
                            resite.setNowresiteid(storeRecite.getId());
                            resite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                            resite.setDeleted(0);
                            resite.setLearn(learn);
                            resite.setReview1("");
                            resite.setReview2("");
                            resite.setReview4("");
                            resite.setReview7("");
                            resite.setReview15("");
                            resite.setOver("");
                            boolean result3 =  resiteService.save(resite);
                            if (result3){
                                returnVO.setCode(200);
                                returnVO.setMessage("添加成功2");
                                return returnVO;
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("存储resite错误");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("存储freshword错误");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("查询词库错误");
                        return returnVO;
                    }





                }


            }
        }
    }


    @RequestMapping(value = "/reciteCiku",method = RequestMethod.POST)
    public ReturnVO reciteCiku(
            @RequestParam("cikutypeid") Integer cikuTypeId
            ,@RequestParam("cikuid")Integer cikuId
            ,@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (cikuId==null||cikuTypeId==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else {
            HashMap<String,Integer> param = new HashMap<>();
            param.put("cikutypeid",cikuTypeId);
            param.put("cikuid",cikuId);
            param.put("type",0);
            param.put("uid",uid);
            QueryWrapper<Nowresite> wrapper = new QueryWrapper<>();
            wrapper.allEq(param);
            Nowresite nowresite = nowresiteService.getOne(wrapper);
            if (nowresite!=null){
                //查找之前正在背的书 将isstudy置为0 并将现在的要背的书isstudy置为1
                HashMap<String,Integer> param2 = new HashMap<>();
                param2.put("isstudy",1);
                param2.put("uid",uid);
                QueryWrapper<Nowresite> wrapper2 = new QueryWrapper<>();
                wrapper2.allEq(param2);
                Nowresite beforenowresite = nowresiteService.getOne(wrapper2);
                if (beforenowresite!=null){
                    beforenowresite.setIsstudy(0);
                    boolean result = nowresiteService.updateById(beforenowresite);
                    if (result){
                        nowresite.setIsstudy(1);
                        boolean result2 = nowresiteService.updateById( nowresite);
                        if (result2){
                            returnVO.setCode(200);
                            returnVO.setMessage("添加成功1");
                            return returnVO;
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("更新NowResite词库错误");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("更新beforeNowResite词库错误");
                        return returnVO;
                    }
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("查询beforeNowResite词库错误");
                    return returnVO;
                }
            }else{

                //查找之前正在背的书 将isstudy置为0 并将现在的要背的书插入
                HashMap<String,Integer> param2 = new HashMap<>();
                param2.put("isstudy",1);
                param2.put("uid",uid);
                QueryWrapper<Nowresite> wrapper2 = new QueryWrapper<>();
                wrapper2.allEq(param2);
                Nowresite beforenowresite = nowresiteService.getOne(wrapper2);
                if (beforenowresite!=null){
                    beforenowresite.setIsstudy(0);
                    boolean result = nowresiteService.updateById(beforenowresite);
                    if (result){
                        //插入
                        Nowresite storeRecite = new Nowresite();
                        storeRecite.setCikuid(cikuId);
                        storeRecite.setCikutypeid(cikuTypeId);
                        storeRecite.setUid(uid);
                        storeRecite.setType(0);
                        storeRecite.setDeleted(0);
                        storeRecite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                        storeRecite.setIsstudy(1);
                        boolean result2 =nowresiteService.save(storeRecite);
                        if (result2){
                            ArrayList<Cikuexample> cikuexamples = cikuexampleService.list(cikuTypeId,cikuId);
                            if (cikuexamples!=null&&cikuexamples.size()!=0){
                                String learn="";
                                for (int i = 0; i < cikuexamples.size(); i++) {
                                    if (i==cikuexamples.size()-1){
                                        learn+=(cikuexamples.get(i).getId());
                                    }else{
                                        learn+=(cikuexamples.get(i).getId()+",");
                                    }
                                }
                                learn = base64Util.encodeToString(learn);
                                Resite resite = new Resite();
                                resite.setNowresiteid(storeRecite.getId());
                                resite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                                resite.setDeleted(0);
                                resite.setLearn(learn);
                                resite.setReview1("");
                                resite.setReview2("");
                                resite.setReview4("");
                                resite.setReview7("");
                                resite.setReview15("");
                                resite.setOver("");
                                boolean result3 =  resiteService.save(resite);
                                if (result3){
                                    returnVO.setCode(200);
                                    returnVO.setMessage("添加成功2");
                                    return returnVO;
                                }else{
                                    returnVO.setCode(500);
                                    returnVO.setMessage("存储resite错误");
                                    return returnVO;
                                }
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("存储Nowresite错误");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("查询词库错误");
                            return returnVO;
                        }



                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("更新beforeNowResite词库错误2");
                        return returnVO;
                    }
                }else{

                    //插入
                    Nowresite storeRecite = new Nowresite();
                    storeRecite.setCikuid(cikuId);
                    storeRecite.setCikutypeid(cikuTypeId);
                    storeRecite.setUid(uid);
                    storeRecite.setType(0);
                    storeRecite.setDeleted(0);
                    storeRecite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    storeRecite.setIsstudy(1);
                    boolean result2 =nowresiteService.save(storeRecite);
                    if (result2){
                        ArrayList<Cikuexample> cikuexamples = cikuexampleService.list(cikuTypeId,cikuId);
                        if (cikuexamples!=null&&cikuexamples.size()!=0){
                            String learn="";
                            for (int i = 0; i < cikuexamples.size(); i++) {
                                if (i==cikuexamples.size()-1){
                                    learn+=(cikuexamples.get(i).getId());
                                }else{
                                    learn+=(cikuexamples.get(i).getId()+",");
                                }
                            }
                            learn = base64Util.encodeToString(learn);
                            Resite resite = new Resite();
                            resite.setNowresiteid(storeRecite.getId());
                            resite.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                            resite.setDeleted(0);
                            resite.setLearn(learn);
                            resite.setReview1("");
                            resite.setReview2("");
                            resite.setReview4("");
                            resite.setReview7("");
                            resite.setReview15("");
                            resite.setOver("");
                            boolean result3 =  resiteService.save(resite);
                            if (result3){
                                returnVO.setCode(200);
                                returnVO.setMessage("添加成功2");
                                return returnVO;
                            }else{
                                returnVO.setCode(500);
                                returnVO.setMessage("存储resite错误");
                                return returnVO;
                            }
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("存储Nowresite错误");
                            return returnVO;
                        }
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("查询词库错误");
                        return returnVO;
                    }





                }


            }
        }
    }
}
