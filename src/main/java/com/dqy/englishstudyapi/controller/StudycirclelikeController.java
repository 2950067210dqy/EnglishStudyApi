package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.service.StudycirclelikeService;
import com.dqy.englishstudyapi.tablebean.Studycirclelike;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-24
 */
@RestController
@RequestMapping("studycirclelike")
public class StudycirclelikeController {
        @Autowired
    StudycirclelikeService studycirclelikeService;
        @Autowired
    TimeUtil timeUtil;

        ReturnVO returnVO;

        @PostMapping("/get")
        public ReturnVO get(@RequestParam("uid") Integer uid){
            returnVO = new ReturnVO();
            if (uid!=null){
                ArrayList<Studycirclelike> studycirclelikes = (ArrayList<Studycirclelike>) studycirclelikeService.list(new QueryWrapper<Studycirclelike>().eq("uid",uid));
                if (studycirclelikes!=null&&studycirclelikes.size()!=0){
                    returnVO.setCode(200);
                    returnVO.setMessage("获取成功");
                    returnVO.setData(studycirclelikes);
                    return  returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("获取数据失败，或暂未存储");
                    return  returnVO;
                }
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("数据为空");
                return  returnVO;
            }
        }

    @PostMapping("/set")
    public ReturnVO set(@RequestParam("uid") Integer uid,@RequestParam("sid")Integer sid){
        returnVO = new ReturnVO();
        if (uid!=null||sid!=null){
            Studycirclelike studycirclelike = new Studycirclelike();
            studycirclelike.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            studycirclelike.setDeleted(0);
            studycirclelike.setUid(uid);
            studycirclelike.setSid(sid);
            boolean result = studycirclelikeService.save(studycirclelike);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("收藏成功");
                return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("收藏失败");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }

    @PostMapping("/delete")
    public ReturnVO delete(@RequestParam("uid") Integer uid,@RequestParam("sid")Integer sid){
        returnVO = new ReturnVO();
        if (uid!=null||sid!=null){
            Map<String,Object> params = new HashMap<>();
            params.put("uid",uid);
            params.put("sid",sid);
            boolean result = studycirclelikeService.remove(new QueryWrapper<Studycirclelike>().allEq(params));
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("取消收藏成功");
                return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("取消收藏失败");
                return  returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
    }
}
