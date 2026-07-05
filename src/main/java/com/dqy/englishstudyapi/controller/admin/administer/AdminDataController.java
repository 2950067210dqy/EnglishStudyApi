package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontDatas;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

@RestController
@RequestMapping("adminData")
public class AdminDataController {
    @Autowired
    UserService userService;

    @Autowired
    WordService wordService;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    CikutypeService cikutypeService;
    @Autowired
    CikuService cikuService;
    @Autowired
    ReadService readService;
    @Autowired
    ReadtypesubService readtypesubService;
    @Autowired
    ReadtypeService readtypeService;
    @Autowired
    TestService testService;
    @Autowired
    TesttypeService testtypeService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    ZborderService zborderService;
    @Autowired
    ProductsService productsService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    CommentService commentService;

    @Autowired
    StudycircleService studycircleService;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    @Autowired
    ListUtil listUtil;
    ReturnVO returnVO;
    @PostMapping("/getAllData")
    public ReturnVO getAllData(){
        returnVO = new ReturnVO();
        List<FrontDatas> frontDatas = new ArrayList<>();

        frontDatas = getUserPart(frontDatas);
        frontDatas = getCikuPart(frontDatas);
        frontDatas = getWordPart(frontDatas);
        frontDatas = getReadPart(frontDatas);
        frontDatas = getTestPart(frontDatas);
        frontDatas = getScorePart(frontDatas);
        frontDatas = getProductPart(frontDatas);
        frontDatas = getOrderPart(frontDatas);
        frontDatas = getStudyCirclePart(frontDatas);

        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(frontDatas);
        return returnVO;
    }

    private List<FrontDatas> getStudyCirclePart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        Long count = studycircleService.count();
        num+=count==null?0:count;
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("学习圈数量");
        frontDatas1.setIcon("pyq");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);

        Long num2 = 0L;
        Long count2 = commentService.count();
        num2+=count2==null?0:count2;
        FrontDatas frontDatas2 = new FrontDatas();
        frontDatas2.setDsc("学习圈评论数量");
        frontDatas2.setIcon("pyq");
        frontDatas2.setNum(num2);
        frontDatas.add(frontDatas2);
        return frontDatas;
    }

    private List<FrontDatas> getOrderPart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        Long count = ordersService.count();
        num+=count==null?0:count;
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("订单数量");
        frontDatas1.setIcon("calendar-filled");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);
        return frontDatas;
    }

    private List<FrontDatas> getProductPart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        Long count = productsService.count();
        num+=count==null?0:count;
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("商品数量");
        frontDatas1.setIcon("gift-filled");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);
        return frontDatas;
    }

    private List<FrontDatas> getScorePart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        List<Score> scores = scoreService.list();
        if (scores!=null){
            for (Score score: scores
                 ) {
                num += score.getScore();
            }
        }
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("积分总量");
        frontDatas1.setIcon("smallcircle");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);

        Long num2 = 0L;
        List<Zborder> zborders = zborderService.list(new QueryWrapper<Zborder>().eq("status",1));
        if (zborders!=null){
            for (Zborder zborder:zborders
                 ) {
                num2 += zborder.getMoney().longValue();
            }
        }
        FrontDatas frontDatas2 = new FrontDatas();
        frontDatas2.setDsc("流水总量");
        frontDatas2.setIcon("smallcircle");
        frontDatas2.setNum(num2);
        frontDatas.add(frontDatas2);
        return frontDatas;
    }

    private List<FrontDatas> getTestPart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        List<Testtype> testtypes = testtypeService.list();
        if (testtypes!=null){
            for (Testtype testtype: testtypes
                 ) {
                testService.createTableIfNotExist(testtype.getId());
                dynamicTableNameUtil.SetTableName("test","_"+testtype.getId());
                Long count = testService.count() ;
                num +=count==null?0:count;

            }
        }
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("题目数量");
        frontDatas1.setIcon("compose");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);

        return frontDatas;
    }

    private List<FrontDatas> getReadPart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        List<Readtype> readtypes = readtypeService.list();
        if (readtypes!=null){
            for (Readtype readtype:readtypes
                 ) {
                readtypesubService.createTable(readtype.getId());
                dynamicTableNameUtil.SetTableName("readtypesub","_"+readtype.getId());
                List<Readtypesub> readtypesubs = readtypesubService.list();
                if (readtypesubs!=null){
                    for (Readtypesub readtypesub:readtypesubs
                         ) {
                        readService.createTable(readtype.getId(),readtypesub.getId());
                        dynamicTableNameUtil.SetTableName("read","_"+readtype.getId()+"_"+readtypesub.getId());
                        Long count = readService.count();
                        num += count==null?0:count;
                    }
                }
            }
        }
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("阅读文章数量");
        frontDatas1.setIcon("chatboxes-filled");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);
        return  frontDatas;
    }

    private List<FrontDatas> getWordPart(List<FrontDatas> frontDatas) {
        Long num = 0L;
        for (Character initial:listUtil.abs
             ) {
            wordService.createTable(initial.toString());
            dynamicTableNameUtil.SetTableName("word","_"+initial.toString());
            Long count = wordService.count();
            num +=count==null?0:count;
        }
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("单词数量");
        frontDatas1.setIcon("font");
        frontDatas1.setNum(num);
        frontDatas.add(frontDatas1);
        return frontDatas;
    }

    private List<FrontDatas> getCikuPart(List<FrontDatas> frontDatas) {
        Long num = 0L ;
        Long num2 = 0L;
        List<Cikutype> cikutypes = cikutypeService.list();
        if (cikutypes!=null){
            for (Cikutype cikutype:cikutypes
                 ) {
                cikuService.createTable(cikutype.getId());
                dynamicTableNameUtil.SetTableName("ciku","_"+cikutype.getId());
                List<Ciku> cikus = cikuService.list();
                num2 +=(cikus==null?0:cikus.size());
                if (cikus!=null){
                    for (Ciku ciku:cikus
                         ) {
                        cikuexampleService.createTable(cikutype.getId(),ciku.getId());
                        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutype.getId()+"_"+ciku.getId());
                        Long count = cikuexampleService.count();
                        num += (count==null?0:count);
                    }
                }
            }
        }
        FrontDatas frontDatas2 = new FrontDatas();
        frontDatas2.setDsc("词库数量");
        frontDatas2.setIcon("wallet-filled");
        frontDatas2.setNum(num2);
        frontDatas.add(frontDatas2);

//        FrontDatas frontDatas1 = new FrontDatas();
//        frontDatas1.setDsc("词库单词数量");
//        frontDatas1.setIcon("wallet-filled");
//        frontDatas1.setNum(num);
//        frontDatas.add(frontDatas1);
        return frontDatas;
    }

    private List<FrontDatas> getUserPart(List<FrontDatas> frontDatas) {
        Long num =  userService.count(new QueryWrapper<User>().eq("type",0));
        FrontDatas frontDatas1 = new FrontDatas();
        frontDatas1.setDsc("用户数量");
        frontDatas1.setIcon("staff-filled");
        frontDatas1.setNum(num==null?0:num);
        frontDatas.add(frontDatas1);
        Long num2 = userService.count(new QueryWrapper<User>().eq("type",1));
        FrontDatas frontDatas2 = new FrontDatas();
        frontDatas2.setDsc("管理员数量");
        frontDatas2.setIcon("staff-filled");
        frontDatas2.setNum(num2==null?0:num2);
        frontDatas.add(frontDatas2);
        return  frontDatas;
    }
}
