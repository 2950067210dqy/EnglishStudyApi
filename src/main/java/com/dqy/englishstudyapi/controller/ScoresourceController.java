package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.frontEntity.FrontScoreSource;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.ScoresourceService;
import com.dqy.englishstudyapi.service.ScoresourcetypeService;
import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.Scoresource;
import com.dqy.englishstudyapi.tablebean.Scoresourcetype;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@RestController
@RequestMapping("scoresource")
public class ScoresourceController {
    @Autowired
    ScoresourceService scoresourceService;
    @Autowired
    ScoresourcetypeService scoresourcetypeService;
    ReturnVO returnVO;
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public ReturnVO get(@RequestParam("uid")Integer uid,@RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Page<Scoresource> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        IPage<Scoresource> scoresourceIPage =  scoresourceService.page(page,new QueryWrapper<Scoresource>().eq("uid",uid).orderByDesc("createtime"));
//        ArrayList<Scoresource> scoresources = new ArrayList<>(scoresourceService.list(new QueryWrapper<Scoresource>().eq("uid",uid)));

        if (scoresourceIPage.getRecords()!=null&&scoresourceIPage.getRecords().size()!=0){
            ArrayList<Scoresource> scoresources= (ArrayList<Scoresource>) scoresourceIPage.getRecords();
            ArrayList<FrontScoreSource> frontScoreSources = new ArrayList<>();
            for (Scoresource s:scoresources
                 ) {
               Scoresourcetype scoresourcetype =  scoresourcetypeService.getById(s.getSourceid());
               FrontScoreSource  frontScoreSource = new FrontScoreSource();
               if (scoresourcetype!=null){
                   frontScoreSource.setDsc(scoresourcetype.getDsc());
                   frontScoreSource.setType(scoresourcetype.getType());
                }else{
                   frontScoreSource.setDsc("");
                   frontScoreSource.setType(0);
               }
               frontScoreSource.setScoresource(s);

               frontScoreSources.add(frontScoreSource);
            }
            MyPage<FrontScoreSource> myPage = new MyPage<>();
            myPage.setData(frontScoreSources);
            myPage.setPageSize(Math.toIntExact(scoresourceIPage.getSize()));
            myPage.setTotal(Math.toIntExact(scoresourceIPage.getTotal()));
            myPage.setCurrent(Math.toIntExact(scoresourceIPage.getCurrent()));
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( myPage);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }
}
