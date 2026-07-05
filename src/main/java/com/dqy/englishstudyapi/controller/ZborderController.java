package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.frontEntity.FrontScoreSource;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.ZborderService;
import com.dqy.englishstudyapi.tablebean.Scoresource;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.timetask.ZbOrderTimeTask;
import com.dqy.englishstudyapi.timetask.ZbOrderTimer;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-20
 */
@RestController
@RequestMapping("zborder")
public class ZborderController {
    @Autowired
    ZborderService zborderService;
    @Autowired
    TimeUtil timeUtil;
//    @Autowired
//    ZbOrderTimer zbOrderTimer;
//    ZbOrderTimeTask zbOrderTimeTask;
    ReturnVO returnVO;

    @PostMapping("/get")
    public ReturnVO get(@RequestParam("uid")Integer uid,@RequestParam(value = "current",defaultValue = "1",required = false)Integer current,@RequestParam(value = "size",defaultValue = "3",required = false)Integer size){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            Page<Zborder> page = new Page<>();
            page.setCurrent(current);
            page.setSize(size);
            IPage<Zborder> ZborderIPage =  zborderService.page(page,new QueryWrapper<Zborder>().eq("uid",uid).orderByDesc("createtime"));
//            ArrayList<Zborder> zborders = (ArrayList<Zborder>) zborderService.list(new QueryWrapper<Zborder>().eq("uid",uid));

            if (ZborderIPage.getRecords()!=null&&ZborderIPage.getRecords().size()!=0){
                ArrayList<Zborder> zborders = (ArrayList<Zborder>) ZborderIPage.getRecords();
                MyPage<Zborder> myPage = new MyPage<>();
                myPage.setData(zborders);
                myPage.setPageSize(Math.toIntExact(ZborderIPage.getSize()));
                myPage.setTotal(Math.toIntExact(ZborderIPage.getTotal()));
                myPage.setCurrent(Math.toIntExact(ZborderIPage.getCurrent()));
                returnVO.setCode(200);
                returnVO.setData(myPage);
                returnVO.setMessage("获取成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败");
                return returnVO;
            }
        }
    }

    @PostMapping("/getNotPay")
    public ReturnVO getNotPay(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{

            ArrayList<Zborder> zborders = (ArrayList<Zborder>) zborderService.list(new QueryWrapper<Zborder>().and(i->i.eq("uid",uid).eq("status",0))  );
            if (zborders!=null&&zborders.size()!=0){

                for (Zborder z:zborders
                     ) {
                    if(timeUtil.differDay(z.getCreatetime(),timeUtil.getNowLocalDateTime())>=1L){
                        z.setStatus(2);
                    }
                }
                zborderService.updateBatchById(zborders);
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取失败");
                return returnVO;
            }
        }
    }

    @PostMapping("/cancelOrder")
    public ReturnVO get(@RequestParam(value = "id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            SubReturnVo subReturnVo = zborderService.cancel(id);
            if (subReturnVo.isResult()){
                returnVO.setCode(subReturnVo.getCode());
                returnVO.setMessage(subReturnVo.getMessage());
                return returnVO;
            }else{
               returnVO.setCode(subReturnVo.getCode());
               returnVO.setMessage(subReturnVo.getMessage());
               return returnVO;
            }

        }
    }

    @PostMapping("/set")
    public ReturnVO get(@RequestBody Zborder zborder){
        returnVO = new ReturnVO();
        if (zborder==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }else{
            zborder.setDeleted(0);
            zborder.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            Map<String,Object> params = new HashMap<>();
            params.put("uid",zborder.getUid());
            params.put("zborderid",zborder.getZborderid());
            boolean result =zborderService.saveOrUpdate(zborder,new QueryWrapper<Zborder>().allEq(params));
            if (result){
                // 初始化时间轮
                // 注册此定时任务（延迟时间为5秒，也就是说5秒后订单过期
//                zbOrderTimeTask = new ZbOrderTimeTask(zborder.getId());
//                zbOrderTimer.newTimeout(zbOrderTimeTask,5, TimeUnit.SECONDS);

                returnVO.setCode(200);
                returnVO.setMessage("存储成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("存储失败");
                return returnVO;
            }
        }
    }
}
