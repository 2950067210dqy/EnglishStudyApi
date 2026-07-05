package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.frontEntity.ReciteRank;
import com.dqy.englishstudyapi.service.RecitedatasumService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Recitedatasum;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@RestController
@RequestMapping("recitedatasum")
public class RecitedatasumController {
    @Autowired
    RecitedatasumService recitedatasumService;
    @Autowired
    UserService userService;
    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;

    @PostMapping("/getRank")
    public  ReturnVO getRank(){
        returnVO = new ReturnVO();
        List<String> orderbyColum = new ArrayList<>();
        orderbyColum.add("num");
        orderbyColum.add("num2");
        orderbyColum.add("time");
        List<Recitedatasum> recitedatasums =  recitedatasumService.list(new QueryWrapper<Recitedatasum>().orderByDesc(orderbyColum).last(" limit 0,10"));

        if (recitedatasums!=null&&recitedatasums.size()!=0){
            ArrayList<ReciteRank> reciteRanks = new ArrayList<>();
            for (Recitedatasum rds:recitedatasums
                 ) {
                ReciteRank reciteRank = new ReciteRank();
                User user = userService.getById(rds.getUid());
                reciteRank.setUser(user);
                reciteRank.setRecitedatasum(rds);
                reciteRanks.add(reciteRank);
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(reciteRanks);
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取数据失败或为空");
            return  returnVO;
        }

    }


    @PostMapping("/set")
    public  ReturnVO set(@RequestBody Recitedatasum recitedatasum){
        returnVO = new ReturnVO();
        if (recitedatasum==null){
            returnVO.setCode(200);
            returnVO.setMessage("存储成功");
            return  returnVO;
        }else{
            SubReturnVo subReturnVo = recitedatasumService.setData(recitedatasum);
            if (subReturnVo.isResult()){
                returnVO.setCode(200);
                returnVO.setMessage("存储总数成功");
                return  returnVO;
            }else{
                returnVO.setCode(subReturnVo.getCode());
                returnVO.setMessage(subReturnVo.getMessage());
                return  returnVO;
            }
        }
    }
}
