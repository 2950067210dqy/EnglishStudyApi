package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ReadCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.TestCondition;
import com.dqy.englishstudyapi.entity.frontEntity.ReadFront.ReadFront;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminTest")
public class AdminTestController {
    @Autowired
    TesttypeService testtypeService;

    @Autowired
    TestService testService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;

    ReturnVO returnVO;
    @PostMapping("/deleteTestSingle")
    public ReturnVO deleteTestSingle(@RequestParam("testType")Integer tesType,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (tesType==null||id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        testService.createTableIfNotExist(tesType);
        dynamicTableNameUtil.SetTableName("test","_"+tesType.toString());
        boolean result = testService.removeById(id);
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }

    }
    @PostMapping("/deleteTestBatch")
    public ReturnVO deleteTestBatch(@RequestParam("ids") List<Integer> ids,@RequestParam("testType")Integer testType){
        returnVO = new ReturnVO();
        if (testType==null||ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        testService.createTableIfNotExist(testType);
        dynamicTableNameUtil.SetTableName("test","_"+testType.toString());
        boolean result = testService.removeBatchByIds(ids);
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }
    }
    @PostMapping("/insert")
    public ReturnVO insert(@RequestParam("test")String testStr,@RequestParam("testType")Integer testType){
        returnVO =  new ReturnVO();
        if (testStr==null||testStr.equals("")||testType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Test test = (Test) jsonUtil.parseJsonStrToJavaObject(testStr,Test.class);
        if (test!=null){
            test.setUpdatetime(timeUtil.getNowLocalDateTime());
            test.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            test.setDeleted(0);

            testService.createTableIfNotExist(testType);
            dynamicTableNameUtil.SetTableName("test","_"+testType.toString());
            boolean result =  testService.save(test);
            if (result){

                returnVO.setCode(200);
                returnVO.setMessage("保存题目成功");
                return returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("保存题目失败2");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("保存题目失败1");
            return returnVO;
        }
    }
    @PostMapping("/update")
    public ReturnVO update(@RequestParam("test")String testStr,@RequestParam("testType")Integer testType){
        returnVO =  new ReturnVO();
        if (testStr==null||testStr.equals("")||testType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Test test = (Test) jsonUtil.parseJsonStrToJavaObject(testStr,Test.class);
        if (test!=null){
            test.setUpdatetime(timeUtil.getNowLocalDateTime());
            testService.createTableIfNotExist(testType);
            dynamicTableNameUtil.SetTableName("test","_"+testType.toString());
            boolean result =  testService.updateById(test);
            if (result){

                returnVO.setCode(200);
                returnVO.setMessage("更新成功");
                return returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("更新题目失败2");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("更新题目失败1");
            return returnVO;
        }

    }

    @PostMapping("/getTestTypeExist")
    public ReturnVO getTestTypeExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Testtype testtype = testtypeService.getOne(new QueryWrapper<Testtype>().eq("dsc",dsc));
        if (testtype!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/deleteTestType")
    public ReturnVO deleteTestType(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null||id==1){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        boolean result = testtypeService.removeById(id);
        if (result){
            testService.createTableIfNotExist(id);
            dynamicTableNameUtil.SetTableName("test","_"+id.toString());
            long count = testService.count();
            if (count!=0){
                testService.createTableIfNotExist(id);
                dynamicTableNameUtil.SetTableName("test","_"+id.toString());
                boolean result2 =testService.remove(new QueryWrapper<>());
                if (result2){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除test失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
                return returnVO;
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("删除testtype失败");
            return returnVO;
        }
    }

    @PostMapping("/insertTestType")
    public ReturnVO insertTestType(@RequestBody Testtype testtype){
        returnVO = new ReturnVO();
        if (testtype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        testtype.setCreatetime(timeUtil.getNowLocalDateTime());
        testtype.setDeleted(0);
        boolean result = testtypeService.save(testtype);
        if (result){
            testService.createTableIfNotExist(testtype.getId());
            returnVO.setCode(200);
            returnVO.setMessage("添加成功");
            returnVO.setData(testtype);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败");
            return returnVO;
        }
    }
    @PostMapping("/updateTestType")
    public ReturnVO updateTestType(@RequestBody Testtype testtype){
        returnVO = new ReturnVO();
        if (testtype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        testtype.setUpdatetime(timeUtil.getNowLocalDateTime());
        testtype.setDeleted(0);
        boolean result = testtypeService.updateById(testtype);
        if (result){
            testService.createTableIfNotExist(testtype.getId());
            returnVO.setCode(200);
            returnVO.setMessage("修改成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改失败");
            return returnVO;
        }
    }

    @PostMapping("/getTestType")
    public  ReturnVO getTestType(){
        returnVO = new ReturnVO();
        List<Testtype> testtypes =testtypeService.list();
        if (testtypes!=null&&testtypes.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(testtypes);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }


    @PostMapping("/getOneById")
    public  ReturnVO getOneById(@RequestParam("testType")Integer testType,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (testType==null||id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        testService.createTableIfNotExist(testType);
        dynamicTableNameUtil.SetTableName("test","_"+testType.toString());
        Test test = testService.getById(id);
        if (test!=null){

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( test);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }

    @PostMapping("/getTest")
    public  ReturnVO getTest(@RequestBody TestCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Test> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());
        testService.createTableIfNotExist(condition.getTestType());
        dynamicTableNameUtil.SetTableName("test","_"+condition.getTestType().toString());
        IPage<Test> iPage = testService.page(page,getConditionWrapper(new QueryWrapper<Test>(),condition));
        if (iPage.getRecords()!=null){
            ArrayList<Test> tests =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Test>) iPage.getRecords();

            MyPage<Test> myPage = new MyPage<>();
            myPage.setData(tests);
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(myPage);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }

    private QueryWrapper<Test> getConditionWrapper(QueryWrapper<Test> wrapper, TestCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getTitleSearch()!=null){
            wrapper = wrapper.like("title",condition.getTitleSearch());
        }
        if (condition.getOptaSearch()!=null){
            wrapper = wrapper.like("opta",condition.getOptaSearch());
        }
        if (condition.getOptbSearch()!=null){
            wrapper = wrapper.like("optb",condition.getOptbSearch());
        }
        if (condition.getOptcSearch()!=null){
            wrapper = wrapper.like("optc",condition.getOptcSearch());
        }
        if (condition.getOptdSearch()!=null){
            wrapper = wrapper.like("optd",condition.getOptdSearch());
        }
        if (condition.getAnalySearch()!=null){
            wrapper = wrapper.like("analy",condition.getAnalySearch());
        }
        if (condition.getAnswerSelect()!=null){
            Consumer<QueryWrapper<Test>> consumer = new Consumer<QueryWrapper<Test>>() {
                @Override
                public void accept(QueryWrapper<Test> wrapper1) {
                    for (int i = 0; i < condition.getAnswerSelect().size(); i++) {
                        if (i!=condition.getAnswerSelect().size()-1){
                            wrapper1 = wrapper1.eq("answer",condition.getAnswerSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("answer",condition.getAnswerSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getOrderbyAsc()!=null&&condition.getOrderbyAsc().size()!=0){
            wrapper= wrapper.orderByAsc(condition.getOrderbyAsc());
        }
        List<String> orderbydesc = condition.getOrderbyDesc();
        orderbydesc.add("updatetime");
        condition.setOrderbyDesc(orderbydesc);
        if (condition.getOrderbyDesc()!=null&&condition.getOrderbyDesc().size()!=0){
            wrapper= wrapper.orderByDesc(condition.getOrderbyDesc());
        }
        return  wrapper;
    }
}
