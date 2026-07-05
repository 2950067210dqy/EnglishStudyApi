package com.dqy.englishstudyapi.timetask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.ReciteRank;
import com.dqy.englishstudyapi.service.RecitedatasumService;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

@EnableScheduling
public class ReciteRankTask {

    @Autowired
    RecitedatasumService recitedatasumService;
    @Autowired
    ScoreService scoreService;
    //5秒钟计算一次
//    @Scheduled(fixedDelay = 5000)
    //每天0点计算
    @Scheduled(cron = "0 0 0 * * ?")
    public void computeScoreByRank(){

        List<String> orderbyColum = new ArrayList<>();
        orderbyColum.add("num");
        orderbyColum.add("num2");
        orderbyColum.add("time");
        List<Recitedatasum> recitedatasums =  recitedatasumService.list(new QueryWrapper<Recitedatasum>().orderByDesc(orderbyColum).last(" limit 0,10"));

        if (recitedatasums!=null&&recitedatasums.size()!=0){
            for (int i = 0; i < recitedatasums.size(); i++) {
                //前三名额外加分
                if (i==0){
                    scoreService.setScore(recitedatasums.get(i).getUid(),17,null);
                }else if(i==1){
                    scoreService.setScore(recitedatasums.get(i).getUid(),18,null);
                }else if(i==2){
                    scoreService.setScore(recitedatasums.get(i).getUid(),19,null);
                }
                //进前十加分
                scoreService.setScore(recitedatasums.get(i).getUid(),20,null);

            }

        }

    }
}
