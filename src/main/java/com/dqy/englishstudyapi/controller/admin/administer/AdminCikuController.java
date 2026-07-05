package com.dqy.englishstudyapi.controller.admin.administer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting.CikuSimpleFull;
import com.dqy.englishstudyapi.entity.adminEntity.administer.CikuWordSimple;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontCikus;
import com.dqy.englishstudyapi.entity.adminEntity.condition.CikuCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.UserCondition;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.CikuService;
import com.dqy.englishstudyapi.service.CikuexampleService;
import com.dqy.englishstudyapi.service.CikutypeService;
import com.dqy.englishstudyapi.service.UserService;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Cikutype;
import com.dqy.englishstudyapi.tablebean.User;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminCiku")
public class AdminCikuController {
    @Autowired
    CikuService cikuService;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    CikutypeService cikutypeService;
    @Autowired
    UserService userService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    ReturnVO returnVO;
    @RequestMapping(value = "/deleteCikuBatch",method = RequestMethod.POST)
    public ReturnVO deleteCikuBatch(@RequestParam("cikuTypeId") Integer cikuTypeId,@RequestParam("ids")List<Integer> ids){
        returnVO = new ReturnVO();
        if (ids==null||ids.size()==0){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            cikuService.createTable(cikuTypeId);
            dynamicTableNameUtil.SetTableName("ciku","_"+cikuTypeId);
            boolean result =   cikuService.removeBatchByIds(ids);
            if (result){
                for (Integer id:ids
                     ) {
                    cikuexampleService.createTable(cikuTypeId,id);
                    dynamicTableNameUtil.SetTableName("cikuexample","_"+cikuTypeId+"_"+id);
                    Long count = cikuexampleService.count();
                    if (count!=null&&count!=0){
                        cikuexampleService.createTable(cikuTypeId,id);
                        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikuTypeId+"_"+id);
                        boolean result2 =  cikuexampleService.remove(new QueryWrapper<Cikuexample>());
                        if (result2){
                            returnVO.setCode(200);
                            returnVO.setMessage("删除成功");
                        }else{
                            returnVO.setCode(500);
                            returnVO.setMessage("删除cikuexample失败");
                        }
                    }


                }
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("删除失败");
            }
        }


        return  returnVO;
    }
    @RequestMapping(value = "/deleteCikuSingle",method = RequestMethod.POST)
    public ReturnVO deleteCikuSingle(@RequestParam("cikuTypeId") Integer cikuTypeId,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }else{
            cikuService.createTable(cikuTypeId);
            dynamicTableNameUtil.SetTableName("ciku","_"+cikuTypeId);
            boolean result = cikuService.removeById(id);
            if (result){
                cikuexampleService.createTable(cikuTypeId,id);
                dynamicTableNameUtil.SetTableName("cikuexample","_"+cikuTypeId+"_"+id);
                Long count = cikuexampleService.count();
                if (count!=null&&count!=0){
                    cikuexampleService.createTable(cikuTypeId,id);
                    dynamicTableNameUtil.SetTableName("cikuexample","_"+cikuTypeId+"_"+id);
                    boolean result2 =  cikuexampleService.remove(new QueryWrapper<Cikuexample>());
                    if (result2){
                        returnVO.setCode(200);
                        returnVO.setMessage("删除成功");
                    }else{
                        returnVO.setCode(500);
                        returnVO.setMessage("删除cikuexample失败");
                    }
                }
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("删除失败");
            }
        }


        return  returnVO;
    }
    @PostMapping("/updateCiku")
    public ReturnVO updateCiku(@RequestParam("cikuTypeId") Integer cikuTypeId,@RequestParam("dsc")String dsc,
                               @RequestParam("cikuId") Integer cikuId,@RequestParam("dscabb")String dscabb,
                               @RequestParam("uid")Integer uid
                               ){
        returnVO = new ReturnVO();
        if (uid==null||dsc==null||dsc.trim().equals("")||cikuTypeId==null||cikuId==null||dscabb==null||dscabb.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        cikuService.createTable(cikuTypeId);
        dynamicTableNameUtil.SetTableName("ciku","_"+cikuTypeId);
        Ciku ciku = cikuService.getById(cikuId);
        if ( ciku!=null){
            ciku.setDsc(dsc);
            ciku.setDscabb(dscabb);
            ciku.setUid(uid);
            ciku.setDeleted(0);
            ciku.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
            cikuService.createTable(cikuTypeId);
            dynamicTableNameUtil.SetTableName("ciku","_"+cikuTypeId);
            boolean result =  cikuService.updateById(ciku);
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
            returnVO.setMessage("ciku不存在");
            return returnVO;
        }
    }
    @PostMapping("/getCikuTypeExist")
    public ReturnVO getCikuTypeExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Cikutype cikutype = cikutypeService.getOne(new QueryWrapper<Cikutype>().eq("dsc",dsc));
        if (cikutype!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/getAllCikuTypes")
    public ReturnVO getAllCikuTypes(){
      returnVO = new ReturnVO();
      List<Cikutype> cikutypes =  cikutypeService.list();
      if (cikutypes!=null&&cikutypes.size()>1){

          cikutypes= cikutypes.subList(1,cikutypes.size());
          returnVO.setCode(200);
          returnVO.setMessage("获取成功");
          returnVO.setData(cikutypes);
          return returnVO;
      }else{
          returnVO.setCode(500);
          returnVO.setMessage("获取失败或为空");
          return returnVO;
      }
    }

    @PostMapping("/deleteCikuType")
    public ReturnVO deleteCikuType(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null||id==1){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        boolean result = cikutypeService.removeById(id);
        if (result){
            cikuService.createTable(id);
            dynamicTableNameUtil.SetTableName("ciku","_"+id.toString());
            long count = cikuService.count();
            if (count!=0){
                cikuService.createTable(id);
                dynamicTableNameUtil.SetTableName("ciku","_"+id.toString());
                boolean result2 =cikuService.remove(new QueryWrapper<>());
                if (result2){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除ciku失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
                return returnVO;
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("删除cikutype失败");
            return returnVO;
        }
    }

    @PostMapping("/insertCikuType")
    public ReturnVO insertCikuType(@RequestBody Cikutype cikutype){
        returnVO = new ReturnVO();
        if (cikutype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        cikutype.setCreatetime(timeUtil.getNowLocalDateTime());
        cikutype.setDeleted(0);
        boolean result = cikutypeService.save(cikutype);
        if (result){
            cikuService.createTable(cikutype.getId());
            returnVO.setCode(200);
            returnVO.setMessage("添加成功");
            returnVO.setData(cikutype);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败");
            return returnVO;
        }
    }
    @PostMapping("/updateCikuType")
    public ReturnVO updateCikuType(@RequestBody Cikutype cikutype){
        returnVO = new ReturnVO();
        if (cikutype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        cikutype.setDeleted(0);
        boolean result = cikutypeService.updateById(cikutype);
        if (result){
            cikuService.createTable(cikutype.getId());
            returnVO.setCode(200);
            returnVO.setMessage("修改成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改失败");
            return returnVO;
        }
    }
    @PostMapping("/getAllCikus")
    public ReturnVO getAllCikus(@RequestBody CikuCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Ciku> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());
        cikuService.createTable(condition.getCikuTypeId());
        dynamicTableNameUtil.SetTableName("ciku","_"+condition.getCikuTypeId().toString());
        IPage<Ciku> iPage = cikuService.page(page,getConditionWrapper(new QueryWrapper<Ciku>(),condition));
        if (iPage.getRecords()!=null){
            List<Ciku> cikus =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Ciku>) iPage.getRecords();
            ArrayList<FrontCikus> frontCikuses = new ArrayList<>();
            for (Ciku c:cikus
                 ) {
                FrontCikus frontCikus = new FrontCikus();
                frontCikus.setCiku(c);
                frontCikus.setUser(userService.getById(c.getUid()));
                dynamicTableNameUtil.SetTableName("cikuexample","_"+condition.getCikuTypeId().toString()+"_"+c.getId().toString());
                frontCikus.setWordCount(Math.toIntExact(cikuexampleService.count()));
                frontCikuses.add(frontCikus);
            }
            MyPage<FrontCikus> myPage = new MyPage<>();
            myPage.setData(frontCikuses);
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

    @PostMapping("/deleteSingle")
    public ReturnVO deleteSingle(@RequestParam("word") String word,@RequestParam("cikuTypeId")Integer cikutypeid,@RequestParam("cikuId")Integer cikuid){
        returnVO = new ReturnVO();
        if (word==null||word.equals("")||cikutypeid==null||cikuid==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        CikuWordSimple wordSimple = (CikuWordSimple) jsonUtil.parseJsonStrToJavaObject(word,CikuWordSimple.class);
        cikuexampleService.createTable(cikutypeid,cikuid);
        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
        boolean result = cikuexampleService.remove(new QueryWrapper<Cikuexample>().eq("initial",wordSimple.getInitial().toLowerCase()).eq("wid",wordSimple.getWid()));
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }

    }
    @PostMapping("/deleteBatch")
    public ReturnVO deleteBatch(@RequestParam("words") String words,@RequestParam("cikuTypeId")Integer cikutypeid,@RequestParam("cikuId")Integer cikuid){
        returnVO = new ReturnVO();
        if (words==null||words.equals("")||cikutypeid==null||cikuid==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        List<CikuWordSimple> wordSimples = jsonUtil.parseJsonStrToArrayList(words, CikuWordSimple.class);
        List<String> initials = new ArrayList<>();
        List<Integer> wids =  new ArrayList<>();
        for (CikuWordSimple word:wordSimples
             ) {
            initials.add( word.getInitial().toLowerCase());
           wids.add(word.getWid());
        }
        cikuexampleService.createTable(cikutypeid,cikuid);
        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
        List<Cikuexample> cikuexamples =cikuexampleService.list(new QueryWrapper<Cikuexample>().and(item->item.in("initial",initials).in("wid",wids)));
        cikuexampleService.createTable(cikutypeid,cikuid);
        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
        boolean result = cikuexampleService.removeBatchByIds(cikuexamples);
        if (result){
            returnVO.setMessage("删除成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("删除失败");
            returnVO.setCode(500);
            return  returnVO;
        }
    }

    @PostMapping("/insertBatch")
    public ReturnVO insertBatch(@RequestParam("words") String words,@RequestParam("cikuTypeId")Integer cikutypeid,@RequestParam("cikuId")Integer cikuid){
        returnVO = new ReturnVO();
        if (words==null||words.equals("")||cikutypeid==null||cikuid==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        List<CikuWordSimple> wordSimples = jsonUtil.parseJsonStrToArrayList(words, CikuWordSimple.class);
        List<String> initials = new ArrayList<>();
        List<Integer> wids =  new ArrayList<>();
        for (CikuWordSimple word:wordSimples
        ) {
            initials.add( word.getInitial().toLowerCase());
            wids.add(word.getWid());
        }
        cikuexampleService.createTable(cikutypeid,cikuid);
        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
        List<Cikuexample> cikuexamples =cikuexampleService.list(new QueryWrapper<Cikuexample>().and(item->item.in("initial",initials).in("wid",wids)));
        List<Cikuexample> saves = new ArrayList<>();
        for (CikuWordSimple cws:wordSimples
             ) {
            boolean flag =true;
            for (Cikuexample ce:cikuexamples
                 ) {
                if(ce.getInitial().toLowerCase().equals(cws.getInitial().toLowerCase())&& Objects.equals(ce.getWid(),cws.getWid())){
                    flag=false;
                    break;
                }
            }
            if (flag){
                Cikuexample cikuexample = new Cikuexample();
                cikuexample.setDeleted(0);
                cikuexample.setInitial(cws.getInitial().toLowerCase());
                cikuexample.setWid(cws.getWid());
                cikuexample.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                saves.add(cikuexample);
            }
        }
        cikuexampleService.createTable(cikutypeid,cikuid);
        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
        boolean result = cikuexampleService.saveBatch(saves);
        if (result){
            returnVO.setMessage("添加成功");
            returnVO.setCode(200);
            return  returnVO;
        }else{
            returnVO.setMessage("添加失败");
            returnVO.setCode(500);
            return  returnVO;
        }

    }

    @PostMapping("/update")
    public ReturnVO update(@RequestParam("word") String word,@RequestParam("oldword") String oldword,@RequestParam("cikuTypeId")Integer cikutypeid,@RequestParam("cikuId")Integer cikuid){
        returnVO = new ReturnVO();
        if (word==null||word.equals("")||word.equals("[]")||oldword==null||oldword.equals("")||oldword.equals("[]")||cikutypeid==null||cikuid==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        CikuWordSimple wordSimple = (CikuWordSimple) jsonUtil.parseJsonStrToJavaObject(oldword,CikuWordSimple.class);
        cikuexampleService.createTable(cikutypeid,cikuid);
        dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
        Cikuexample cikuexample = cikuexampleService.getOne(new QueryWrapper<Cikuexample>().eq("initial",wordSimple.getInitial().toLowerCase()).eq("wid",wordSimple.getWid()));
        if (cikuexample!=null){
            CikuWordSimple newwordSimple = (CikuWordSimple) jsonUtil.parseJsonStrToJavaObject(word,CikuWordSimple.class);
            cikuexample.setUpdatetime(timeUtil.getCurrentTimeLocalDateTime());
            cikuexample.setInitial(newwordSimple.getInitial().toLowerCase());
            cikuexample.setWid(newwordSimple.getWid());
            cikuexampleService.createTable(cikutypeid,cikuid);
            dynamicTableNameUtil.SetTableName("cikuexample","_"+cikutypeid+"_"+cikuid);
            boolean result = cikuexampleService.updateById(cikuexample);
            if (result){
                returnVO.setMessage("修改成功");
                returnVO.setCode(200);
                return  returnVO;
            }else{
                returnVO.setMessage("修改失败");
                returnVO.setCode(500);
                return  returnVO;
            }
        }else{
            returnVO.setMessage("获取失败");
            returnVO.setCode(500);
            return  returnVO;
        }


    }
    private QueryWrapper<Ciku> getConditionWrapper(QueryWrapper<Ciku> wrapper, CikuCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getDscabbSearch()!=null){
            wrapper = wrapper.like("dscabb",condition.getDscabbSearch());
        }
        if (condition.getDscSearch()!=null){
            wrapper = wrapper.like("dsc",condition.getDscSearch());
        }
        if (condition.getUserSelect()!=null){
            Consumer<QueryWrapper<Ciku>> consumer = new Consumer<QueryWrapper<Ciku>>() {
                @Override
                public void accept(QueryWrapper<Ciku> wrapper1) {
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
}
