package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontScores;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontWords;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontZbOrders;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ScoreCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ScoreSourceTypeCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.TestCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ZbOrderCondition;
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
@RequestMapping("adminScore")
public class AdminScoreController {
    @Autowired
    ScoresourcetypeService scoresourcetypeService;

    @Autowired
    ScoreService scoreService;
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ZborderService zborderService;
    @Autowired
    UserService userService;
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

    @PostMapping("/getZbOrder")
    public  ReturnVO getZbOrder(@RequestBody ZbOrderCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Zborder> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<Zborder> iPage = zborderService.page(page,getConditionWrapper(new QueryWrapper<Zborder>(),condition));
        if (iPage.getRecords()!=null){
            ArrayList<Zborder> zborders =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Zborder>) iPage.getRecords();
            ArrayList<FrontZbOrders> frontZbOrders = new ArrayList<>();
            for (Zborder z:zborders
            ) {
                FrontZbOrders frontZborder = new FrontZbOrders();
                frontZborder.setZborder(z);
                User user = userService.getById(z.getUid());
                frontZborder.setUser(user);
                frontZbOrders.add(frontZborder);
            }
            MyPage<FrontZbOrders> myPage = new MyPage<>();
            myPage.setData(frontZbOrders);
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

    @PostMapping("/updateScore")
    public ReturnVO updateScore(@RequestBody Score score){
        returnVO = new ReturnVO();
        if (score==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Score oldScore = scoreService.getById(score.getId());
        if (oldScore!=null){

           Long diff = score.getScore()-oldScore.getScore();
           if (diff!=0){
               Scoresource scoresource = new Scoresource();
               scoresource.setUpdatetime(timeUtil.getNowLocalDateTime());
               scoresource.setDeleted(0);
               scoresource.setCreatetime(timeUtil.getNowLocalDateTime());
               scoresource.setUid(score.getUid());
               if (diff<0){
                   scoresource.setSourceid(16);
               }else{
                   scoresource.setSourceid(15);
               }
               scoresource.setNum(Math.abs(diff));
               boolean result = scoresourceService.save(scoresource);
               if (result){
                   score.setUpdatetime(timeUtil.getNowLocalDateTime());
                   result = scoreService.updateById(score);
                   if (result){
                       returnVO.setCode(200);
                       returnVO.setMessage("修改成功");
                       return returnVO;
                   }else{
                       returnVO.setCode(500);
                       returnVO.setMessage("修改失败");
                       return returnVO;
                   }
               }else{
                   returnVO.setCode(500);
                   returnVO.setMessage("添加积分来源错误");
                   return returnVO;
               }
           }else{
               returnVO.setCode(200);
               returnVO.setMessage("修改成功");
               return returnVO;
           }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("账户不存在");
            return returnVO;
        }

    }


    @PostMapping("/getScore")
    public  ReturnVO getScore(@RequestBody ScoreCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Score> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<Score> iPage = scoreService.page(page,getConditionWrapper(new QueryWrapper<Score>(),condition));
        if (iPage.getRecords()!=null){
            ArrayList<Score> scores =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Score>) iPage.getRecords();
            ArrayList<FrontScores> frontScores = new ArrayList<>();
            for (Score c:scores
                 ) {
                FrontScores frontScore = new FrontScores();
                frontScore.setScore(c);
                User user = userService.getById(c.getUid());
                frontScore.setUser(user);
                frontScores.add(frontScore);
            }
            MyPage<FrontScores> myPage = new MyPage<>();
            myPage.setData(frontScores);
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

    @PostMapping("/updateScoreSource")
    public ReturnVO updateScoreSource(@RequestBody Scoresourcetype scoresourcetype){
        returnVO =  new ReturnVO();
        if (scoresourcetype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        scoresourcetype.setUpdatetime(timeUtil.getNowLocalDateTime());


        boolean result =  scoresourcetypeService.updateById(scoresourcetype);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("修改积分类别来源成功");
            return returnVO;

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改积分类别来源失败");
            return returnVO;
        }

    }
    @PostMapping("/insertScoreSource")
    public ReturnVO insertScoreSource(@RequestBody Scoresourcetype scoresourcetype){
        returnVO =  new ReturnVO();
        if (scoresourcetype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        scoresourcetype.setUpdatetime(timeUtil.getNowLocalDateTime());
        scoresourcetype.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
        scoresourcetype.setDeleted(0);

        boolean result =  scoresourcetypeService.save(scoresourcetype);
        if (result){

            returnVO.setCode(200);
            returnVO.setMessage("保存积分类别来源成功");
            return returnVO;

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("保存积分类别来源失败");
            return returnVO;
        }

    }
    @PostMapping("/getOneScoreSourceTypeById")
    public  ReturnVO getOneScoreSourceTypeById(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }



       Scoresourcetype scoresourcetype = scoresourcetypeService.getById(id);
        if (scoresourcetype!=null){

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( scoresourcetype);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }


    @PostMapping("/getScoreSourceTypeExist")
    public ReturnVO getScoreSourceTypeExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Scoresourcetype scoresourcetype = scoresourcetypeService.getOne(new QueryWrapper<Scoresourcetype>().eq("dsc",dsc));
        if (scoresourcetype!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }

    @PostMapping("/getScoreSourceType")
    public  ReturnVO getScoreSourceType(@RequestBody ScoreSourceTypeCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Scoresourcetype> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());

        IPage<Scoresourcetype> iPage = scoresourcetypeService.page(page,getConditionWrapper(new QueryWrapper<Scoresourcetype>(),condition));
        if (iPage.getRecords()!=null){
            ArrayList<Scoresourcetype> scoresourcetypes =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Scoresourcetype>) iPage.getRecords();

            MyPage<Scoresourcetype> myPage = new MyPage<>();
            myPage.setData(scoresourcetypes);
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

    private QueryWrapper<Zborder> getConditionWrapper(QueryWrapper<Zborder> wrapper, ZbOrderCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getZbcodeSearch()!=null){
            wrapper = wrapper.like("zbcode",condition.getZbcodeSearch());
        }
        if (condition.getZborderidSearch()!=null){
            wrapper = wrapper.like("zborderid",condition.getZborderidSearch());
        }
        if (condition.getStatusSelect()!=null){
            Consumer<QueryWrapper<Zborder>> consumer = new Consumer<QueryWrapper<Zborder>>() {
                @Override
                public void accept(QueryWrapper<Zborder> wrapper1) {
                    for (int i = 0; i < condition.getStatusSelect().size(); i++) {
                        if (i!=condition.getStatusSelect().size()-1){
                            wrapper1 = wrapper1.eq("status",condition.getStatusSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("status",condition.getStatusSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer);
        }
        if (condition.getUserSelect()!=null){
            Consumer<QueryWrapper<Zborder>> consumer2 = new Consumer<QueryWrapper<Zborder>>() {
                @Override
                public void accept(QueryWrapper<Zborder> wrapper1) {
                    for (int i = 0; i < condition.getUserSelect().size(); i++) {
                        if (i!=condition.getUserSelect().size()-1){
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i));
                        }
                    }
                }
            };
            wrapper.and(consumer2);
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
    private QueryWrapper<Score> getConditionWrapper(QueryWrapper<Score> wrapper, ScoreCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }

        if (condition.getUserSelect()!=null){
            Consumer<QueryWrapper<Score>> consumer = new Consumer<QueryWrapper<Score>>() {
                @Override
                public void accept(QueryWrapper<Score> wrapper1) {
                    for (int i = 0; i < condition.getUserSelect().size(); i++) {
                        if (i!=condition.getUserSelect().size()-1){
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("uid",condition.getUserSelect().get(i));
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
    private QueryWrapper<Scoresourcetype> getConditionWrapper(QueryWrapper<Scoresourcetype> wrapper, ScoreSourceTypeCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getDscSearch()!=null){
            wrapper = wrapper.like("dsc",condition.getDscSearch());
        }

        if (condition.getTypeSelect()!=null){
            Consumer<QueryWrapper<Scoresourcetype>> consumer = new Consumer<QueryWrapper<Scoresourcetype>>() {
                @Override
                public void accept(QueryWrapper<Scoresourcetype> wrapper1) {
                    for (int i = 0; i < condition.getTypeSelect().size(); i++) {
                        if (i!=condition.getTypeSelect().size()-1){
                            wrapper1 = wrapper1.eq("type",condition.getTypeSelect().get(i)).or();
                        }else{
                            wrapper1 = wrapper1.eq("type",condition.getTypeSelect().get(i));
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
