package com.dqy.englishstudyapi.timetask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.GamedatamaxService;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.tablebean.Gamedatamax;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

@EnableScheduling
public class GameRankTask {

    @Autowired
    GamedatamaxService gamedatamaxService;
    @Autowired
    ScoreService scoreService;



    //5秒钟计算一次
//    @Scheduled(fixedDelay = 5000)
    //每天0点计算
    @Scheduled(cron = "0 0 0 * * ?")
    public void computeScoreByRank(){
        List<Gamedatamax> gamedatamax = gamedatamaxService.list(new QueryWrapper<Gamedatamax>().orderByDesc("score").last(" limit 0,10"));
        if (gamedatamax!=null&&gamedatamax.size()!=0){
            for (int i = 0; i < gamedatamax.size(); i++) {
                //前三名额外加分
                if (i==0){
                    scoreService.setScore(gamedatamax.get(i).getUid(),21,null);
                }else if(i==1){
                    scoreService.setScore(gamedatamax.get(i).getUid(),22,null);
                }else if(i==2){
                    scoreService.setScore(gamedatamax.get(i).getUid(),23,null);
                }
                //进前十加分
                scoreService.setScore(gamedatamax.get(i).getUid(),24,null);
            }
        }
        gamedatamax = gamedatamaxService.list(new QueryWrapper<Gamedatamax>().orderByDesc("level").last(" limit 0,10"));
        if (gamedatamax!=null&&gamedatamax.size()!=0){
            for (int i = 0; i < gamedatamax.size(); i++) {
                //前三名额外加分
                if (i==0){
                    scoreService.setScore(gamedatamax.get(i).getUid(),25,null);
                }else if(i==1){
                    scoreService.setScore(gamedatamax.get(i).getUid(),26,null);
                }else if(i==2){
                    scoreService.setScore(gamedatamax.get(i).getUid(),27,null);
                }
                //进前十加分
                scoreService.setScore(gamedatamax.get(i).getUid(),28,null);
            }
        }
         gamedatamax = gamedatamaxService.list(new QueryWrapper<Gamedatamax>().orderByDesc("time").last(" limit 0,10"));
        if (gamedatamax!=null&&gamedatamax.size()!=0){
            for (int i = 0; i < gamedatamax.size(); i++) {
                //前三名额外加分
                if (i==0){
                    scoreService.setScore(gamedatamax.get(i).getUid(),29,null);
                }else if(i==1){
                    scoreService.setScore(gamedatamax.get(i).getUid(),30,null);
                }else if(i==2){
                    scoreService.setScore(gamedatamax.get(i).getUid(),31,null);
                }
                //进前十加分
                scoreService.setScore(gamedatamax.get(i).getUid(),32,null);
            }
        }

    }
}
