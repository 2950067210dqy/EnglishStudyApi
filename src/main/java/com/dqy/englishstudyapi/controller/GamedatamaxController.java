package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.GameData.FrontGameData;
import com.dqy.englishstudyapi.service.GamedatamaxService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Gamedata;
import com.dqy.englishstudyapi.tablebean.Gamedatamax;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-04-02
 */
@RestController
@RequestMapping("gamedatamax")
public class GamedatamaxController {
    @Autowired
    GamedatamaxService gamedatamaxService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;
    ReturnVO returnVO;

    @PostMapping("/getByUid")
    public ReturnVO getByUid(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Gamedatamax gamedatamax = gamedatamaxService.getOne(new QueryWrapper<Gamedatamax>().eq("uid",uid));
        if (gamedatamax!=null){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(gamedatamax);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return returnVO;
        }
    }

    @PostMapping("/getByScoreRank")
    public ReturnVO getByScoreRank(){
        returnVO = new ReturnVO();


        List<Gamedatamax> gamedatamax = gamedatamaxService.list(new QueryWrapper<Gamedatamax>().orderByDesc("score").last(" limit 0,10"));
        if (gamedatamax!=null&&gamedatamax.size()!=0){
//            Collections.sort(gamedatamax, new Comparator<Gamedatamax>() {
//                @Override
//                public int compare(Gamedatamax o1, Gamedatamax o2) {
//                    if (o1.getScore()>o2.getScore()){
//                        return  -1;
//                    }else{
//                        return 1;
//                    }
//                }
//            });
            List<FrontGameData> frontGameDatas = new ArrayList<>();
            for (Gamedatamax g:gamedatamax
                 ) {
                FrontGameData frontGameData = new FrontGameData();
                frontGameData.setGamedatamax(g);
                frontGameData.setUser(userService.getById(g.getUid()));
                frontGameDatas.add(frontGameData);
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
//            returnVO.setData( frontGameDatas.size()>10?frontGameDatas.subList(0,10):frontGameDatas);
            returnVO.setData(frontGameDatas);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return returnVO;
        }
    }

    @PostMapping("/getByTimeRank")
    public ReturnVO getByTimeRank(){
        returnVO = new ReturnVO();


        List<Gamedatamax> gamedatamax = gamedatamaxService.list(new QueryWrapper<Gamedatamax>().orderByDesc("time").last(" limit 0,10"));
        if (gamedatamax!=null&&gamedatamax.size()!=0){
//            Collections.sort(gamedatamax, new Comparator<Gamedatamax>() {
//                @Override
//                public int compare(Gamedatamax o1, Gamedatamax o2) {
//                    if (o1.getTime()>o2.getTime()){
//                        return  -1;
//                    }else{
//                        return 1;
//                    }
//                }
//            });
            List<FrontGameData> frontGameDatas = new ArrayList<>();
            for (Gamedatamax g:gamedatamax
            ) {
                FrontGameData frontGameData = new FrontGameData();
                frontGameData.setGamedatamax(g);
                frontGameData.setUser(userService.getById(g.getUid()));
                frontGameDatas.add(frontGameData);
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
//            returnVO.setData(frontGameDatas.size()>10?frontGameDatas.subList(0,10):frontGameDatas);
            returnVO.setData(frontGameDatas);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return returnVO;
        }
    }

    @PostMapping("/getByLevelRank")
    public ReturnVO getByLevelRank(){
        returnVO = new ReturnVO();


        List<Gamedatamax> gamedatamax = gamedatamaxService.list(new QueryWrapper<Gamedatamax>().orderByDesc("level").last(" limit 0,10"));
        if (gamedatamax!=null&&gamedatamax.size()!=0){
//            Collections.sort(gamedatamax, new Comparator<Gamedatamax>() {
//                @Override
//                public int compare(Gamedatamax o1, Gamedatamax o2) {
//                    if (o1.getLevel()>o2.getLevel()){
//                        return  -1;
//                    }else{
//                        return 1;
//                    }
//                }
//            });
            List<FrontGameData> frontGameDatas = new ArrayList<>();
            for (Gamedatamax g:gamedatamax
            ) {
                FrontGameData frontGameData = new FrontGameData();
                frontGameData.setGamedatamax(g);
                frontGameData.setUser(userService.getById(g.getUid()));
                frontGameDatas.add(frontGameData);
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
//            returnVO.setData(frontGameDatas.size()>10?frontGameDatas.subList(0,10):frontGameDatas);
            returnVO.setData(frontGameDatas);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return returnVO;
        }
    }
}
