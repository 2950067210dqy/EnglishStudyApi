package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.TestErrorFull.TestErrorFull;
import com.dqy.englishstudyapi.entity.frontEntity.TestFront;
import com.dqy.englishstudyapi.entity.frontEntity.TestLikeFull.TestLikeFull;
import com.dqy.englishstudyapi.entity.frontEntity.TestTypeFront;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.tablebean.Testerror;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.dqy.englishstudyapi.tablebean.Testtype;
import com.dqy.englishstudyapi.util.RandomUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
 * @since 2023-02-28
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    TestService testService;
    @Autowired
    TestlikeService testlikeService;
    @Autowired
    TesterrorService testerrorService;
    @Autowired
    TesttypeService testtypeService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    RandomUtil randomUtil;
    ReturnVO returnVO;
    @PostMapping("/count")
    public ReturnVO count(@RequestParam("testtype") Integer testtype
                       ){
        returnVO = new ReturnVO();
        if (testtype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Integer count =testService.count(testtype);
        if (count!=null){
            returnVO.setData(count);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/getTestLike")
    public ReturnVO getTestLike(
                             @RequestParam("uid")Integer uid,
                             @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,
                             @RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if (current==null||size==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        List<Testlike> testlikes =testlikeService.list(new QueryWrapper<Testlike>().eq("uid",uid).orderByDesc("updatetime").last("limit "+(current-1)*size+","+size));
        if (testlikes!=null&&testlikes.size()!=0){
            ArrayList<TestLikeFull> testLikeFulls = new ArrayList<>();
            for (Testlike tl:testlikes
                 ) {
                TestLikeFull testLikeFull = new TestLikeFull();
                 testLikeFull.setTestlike(tl);
                 Testtype testtype =   testtypeService.getById(tl.getTesttype()) ;
                 testLikeFull.setTesttype(testtype);
                 Test test =  testService.getById(tl.getTesttype(),tl.getTestid());
                 TestFront testFront = new TestFront();
                 testFront.setIsLike(true);
                 testFront.setTest(test);
                 testLikeFull.setTestFront(testFront);
                 testLikeFulls.add(testLikeFull);
            }

            if ( testLikeFulls!=null&& testLikeFulls.size()!=0){
                returnVO.setData( testLikeFulls);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("收藏获取失败或为空");
            return returnVO;
        }

    }

    @PostMapping("/getTestError")
    public ReturnVO getTestError(
            @RequestParam("uid")Integer uid,
            @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,
            @RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if (current==null||size==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        List<Testerror> testerrors =testerrorService.list(new QueryWrapper<Testerror>().eq("uid",uid).orderByDesc("updatetime").last("limit "+(current-1)*size+","+size));
        if (testerrors!=null&&testerrors.size()!=0){
            ArrayList<TestErrorFull> testerrorFulls = new ArrayList<>();
            for (Testerror tE:testerrors
            ) {
                TestErrorFull testErrorFull = new TestErrorFull();
                testErrorFull.setTesterror(tE);
                Testtype testtype =   testtypeService.getById(tE.getTesttype()) ;
                testErrorFull.setTesttype(testtype);
                Test test =  testService.getById(tE.getTesttype(),tE.getTestid());
                TestFront testFront = new TestFront();
                testFront=testlikeService.setLike(test,uid,testtype.getId());
                testErrorFull.setTestFront(testFront);
                testerrorFulls.add(testErrorFull);
            }

            if ( testerrorFulls!=null&&testerrorFulls.size()!=0){
                returnVO.setData( testerrorFulls);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("收藏获取失败或为空");
            return returnVO;
        }

    }

    @PostMapping("/getLearn")
    public ReturnVO getLearn(@RequestParam("testtype") Integer testtype,
                        @RequestParam("uid")Integer uid,
                        @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,
                        @RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if (current==null||testtype==null||size==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        List<Test> testList =testService.list(testtype,current,size);
        List<TestFront> testFronts = testlikeService.setLike(testList,uid, testtype);
        if (testFronts!=null&&testFronts.size()!=0){
            returnVO.setData(testFronts);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/getExam")
    public ReturnVO getExam(@RequestParam("testtype") Integer testtype,
                            @RequestParam("uid")Integer uid,
                            @RequestParam(value = "current",defaultValue = "1",required = false)Integer current,
                        @RequestParam(value = "size",defaultValue = "20",required = false)Integer size){
        returnVO = new ReturnVO();
        if (current==null||testtype==null||size==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Integer count = testService.count(testtype);
        if (count!=0){
            List<Test> testList=null;
            if (count-size>0){
                Integer random = randomUtil.getRandomRange(0, count-size);
                 testList =testService.listRandom(testtype,random,size);
            }else{
                testList =testService.listRandom(testtype,0,size);
            }


            List<TestFront> testFronts = testlikeService.setLike(testList,uid, testtype);
            if (testFronts!=null&&testFronts.size()!=0){
                returnVO.setData(testFronts);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("题库为空");
            return returnVO;
        }

    }
}
