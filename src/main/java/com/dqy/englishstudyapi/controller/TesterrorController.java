package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dqy.englishstudyapi.service.TesterrorService;
import com.dqy.englishstudyapi.tablebean.Testerror;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
@RestController
@RequestMapping("testerror")
public class TesterrorController {
    @Autowired
    TesterrorService testerrorService;
    @Autowired
    TimeUtil timeUtil;

    ReturnVO returnVO;

    @PostMapping("/count")
    public ReturnVO count(@RequestParam("uid") Integer uid
    ){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Long count = testerrorService.count(new QueryWrapper<Testerror>().eq("uid", uid));
        if (count!=null){
            returnVO.setData(count);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }
    @PostMapping("/setBatch")
    public ReturnVO setBath(@RequestParam("testtype")Integer testtype, @RequestParam("testid") ArrayList<String> testid, @RequestParam("uid")Integer uid){

        returnVO = new ReturnVO();
        if (testtype==null||testid==null||uid==null||testid.size()==0){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        ArrayList<Testerror> testerrors = new ArrayList<>();
        for (String tid:testid
             ) {
            String[] tids =tid.split("@");
            Testerror testerror = new Testerror();
            testerror.setCreatetime(timeUtil.getNowLocalDateTime());
            testerror.setUid(uid);
            testerror.setDeleted(0);
            testerror.setTestid(Integer.valueOf(tids[0]));
            testerror.setChoose(Integer.valueOf(tids[1]));
            testerror.setTesttype(testtype);
            testerrors.add(testerror);
        }
        List<Testerror> testerrorsStore = testerrorService.list(new QueryWrapper<Testerror>().eq("uid",uid));
        if (testerrorsStore!=null&&testerrorsStore.size()!=0){
            Iterator<Testerror> iterable = testerrors.iterator();
            while (iterable.hasNext()){
                Testerror temp = iterable.next();
                for (Testerror store:testerrorsStore
                     ) {
                    if (Objects.equals(temp.getTesttype(),store.getTesttype())&&Objects.equals(temp.getTestid(),store.getTestid())){
                        iterable.remove();
                    }
                }

            }
        }
        boolean result = testerrorService.saveOrUpdateBatch(testerrors);
        if (result){
            returnVO.setCode(200);
            returnVO.setMessage("加入错题本成功");
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("加入错题本收藏失败");
            return  returnVO;
        }

    }
    @PostMapping("/set")
    public ReturnVO set(@RequestParam("testtype")Integer testtype, @RequestParam("testid")Integer testid, @RequestParam("uid")Integer uid,@RequestParam("choose")Integer choose){

        returnVO = new ReturnVO();
        if (testtype==null||testid==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("uid",uid);
        params.put("testtype",testtype);
        params.put("testid",testid);
        Testerror exist =  testerrorService.getOne(new QueryWrapper<Testerror>().allEq(params));
        if (exist==null){
            Testerror testerro = new Testerror();
            testerro.setCreatetime(timeUtil.getNowLocalDateTime());
            testerro.setUid(uid);
            testerro.setDeleted(0);
            testerro.setTestid(testid);
            testerro.setChoose(choose);
            testerro.setTesttype(testtype);
            boolean result =  testerrorService.save(testerro);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("加入错题本成功");
                return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("加入错题本失败");
                return  returnVO;
            }
        }else{
            exist.setUpdatetime(timeUtil.getNowLocalDateTime());
            boolean result = testerrorService.updateById(exist);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("加入错题本成功2");
                return  returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("加入错题本失败2");
                return  returnVO;
            }
        }

    }

    @PostMapping("/delete")
    public ReturnVO delete(@RequestParam("testtype")Integer testtype,@RequestParam("testid")Integer testid,@RequestParam("uid")Integer uid){

        returnVO = new ReturnVO();
        if (testtype==null||testid==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("uid",uid);
        params.put("testid",testid);
        params.put("testtype",testtype);
        boolean result = testerrorService.remove(new QueryWrapper<Testerror>().allEq(params));
        if (result){
            returnVO.setCode(200);
            returnVO.setMessage("取消加入错题本成功");
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("取消加入错题本失败");
            return  returnVO;
        }

    }

    @PostMapping("/get")
    public ReturnVO get(@RequestParam("uid")Integer uid){

        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        SubReturnVo subReturnVo =testerrorService.getFull(uid);
        if ( subReturnVo.isResult()){
            returnVO.setCode(200);
            returnVO.setMessage("获取错题本成功");
            returnVO.setData(subReturnVo.getData());
            return  returnVO;
        }else{
            returnVO.setCode(subReturnVo.getCode());
            returnVO.setMessage(subReturnVo.getMessage());
            return  returnVO;
        }

    }

}
