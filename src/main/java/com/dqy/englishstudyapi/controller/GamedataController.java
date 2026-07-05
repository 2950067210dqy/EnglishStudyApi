package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.frontEntity.ReadFront.ReadFront;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.GamedataService;
import com.dqy.englishstudyapi.service.GamedatamaxService;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.tablebean.Gamedata;
import com.dqy.englishstudyapi.tablebean.Gamedatamax;
import com.dqy.englishstudyapi.tablebean.Read;
import com.dqy.englishstudyapi.util.TimeUtil;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-04-02
 */
@RestController
@RequestMapping("gamedata")
public class GamedataController {

    @Autowired
    GamedataService gamedataService;
    @Autowired
    GamedatamaxService gamedatamaxService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;

    @PostMapping("/getByUid")
    public ReturnVO getByUid(@RequestParam("uid")Integer uid,@RequestParam(value = "current",defaultValue = "1")Integer current,@RequestParam(value = "size",defaultValue = "5")Integer size){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Page<Gamedata> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        IPage<Gamedata> iPage = gamedataService.page(page,new QueryWrapper<Gamedata>().eq("uid",uid).orderByDesc("updatetime"));
        if (iPage.getRecords()!=null&&iPage.getRecords().size()!=0) {
            ArrayList<Gamedata> gamedataList = iPage.getRecords().size() == 0 ? new ArrayList<>() : (ArrayList<Gamedata>) iPage.getRecords();
            MyPage<Gamedata> myPage = new MyPage<>();
            myPage.setData(gamedataList);
            myPage.setPageSize(Math.toIntExact(iPage.getSize()));
            myPage.setTotal(Math.toIntExact(iPage.getTotal()));
            myPage.setCurrent(Math.toIntExact(iPage.getCurrent()));

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( myPage);
            return returnVO;

        }else {
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return returnVO;
        }
    }

    @PostMapping("/setByUid")
    public ReturnVO setByUid(@RequestParam("uid")Integer uid,@RequestParam("score")Integer score,@RequestParam("level")Integer level ,@RequestParam("time")Integer time){
        returnVO = new ReturnVO();
        if (uid==null||score==null||level==null||time==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Gamedata gamedata = new Gamedata();
        gamedata.setUid(uid);
        gamedata.setTime(time);
        gamedata.setLevel(level);
        gamedata.setScore(score);
        gamedata.setCreatedate(timeUtil.getNowLocalDate());
        gamedata.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
        gamedata.setUpdatetime(timeUtil.getNowLocalDateTime());
        gamedata.setDeleted(0);
        boolean result = gamedataService.save(gamedata);
        if (result){
            Gamedatamax gamedatamax = gamedatamaxService.getOne(new QueryWrapper<Gamedatamax>().eq("uid",uid));
            if (gamedatamax==null){
                gamedatamax = new Gamedatamax();
                gamedatamax.setUid(uid);
                gamedatamax.setLevel(level);
                gamedatamax.setScore(score);
                gamedatamax.setDeleted(0);
                gamedatamax.setCreatedate(timeUtil.getCurrentTimeLocalDate());
                gamedatamax.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                gamedatamax.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
                gamedatamax.setTime(time);
                result = gamedatamaxService.save(gamedatamax);
                if (result){
                    SubReturnVo subReturnVo = scoreService.setScore(uid,33,null);
                    if (subReturnVo.isResult()){
                        returnVO.setCode(200);
                        returnVO.setMessage("保存成功");
                        return returnVO;
                    }else{
                        returnVO.setCode(subReturnVo.getCode());
                        returnVO.setMessage(subReturnVo.getMessage());
                        return returnVO;
                    }

                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("保存gamedatamax失败1");
                    return returnVO;
                }
            }else{
                if (gamedatamax.getScore()<score){
                    gamedatamax.setScore(score);
                    gamedatamax.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
                    result = gamedatamaxService.updateById(gamedatamax);
                    if (!result){
                        returnVO.setCode(500);
                        returnVO.setMessage("保存gamedatamax失败2");
                        return returnVO;
                    }
                }
                if (gamedatamax.getLevel()<level){
                    gamedatamax.setLevel(level);
                    gamedatamax.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
                    result = gamedatamaxService.updateById(gamedatamax);
                    if (!result){
                        returnVO.setCode(500);
                        returnVO.setMessage("保存gamedatamax失败2");
                        return returnVO;
                    }
                }
                if (gamedatamax.getTime()<time){
                    gamedatamax.setTime(time);
                    gamedatamax.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
                    result = gamedatamaxService.updateById(gamedatamax);
                    if (!result){
                        returnVO.setCode(500);
                        returnVO.setMessage("保存gamedatamax失败3");
                        return returnVO;
                    }
                }

                SubReturnVo subReturnVo = scoreService.setScore(uid,33,null);
                if (subReturnVo.isResult()){
                    returnVO.setCode(200);
                    returnVO.setMessage("保存成功");
                    return returnVO;
                }else{
                    returnVO.setCode(subReturnVo.getCode());
                    returnVO.setMessage(subReturnVo.getMessage());
                    return returnVO;
                }
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("保存gamedata失败");
            return returnVO;
        }
    }
}
