package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.administer.FrontWords;
import com.dqy.englishstudyapi.entity.adminEntity.condition.ReadCondition;
import com.dqy.englishstudyapi.entity.frontEntity.ReadFront.ReadFront;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.ReadService;
import com.dqy.englishstudyapi.service.ReadtypeService;
import com.dqy.englishstudyapi.service.ReadtypesubService;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("adminRead")
public class AdminReadController {
    @Autowired
    ReadtypeService readtypeService;
    @Autowired
    ReadtypesubService readtypesubService;
    @Autowired
    ReadService readService;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;

    ReturnVO returnVO;
    @PostMapping("/deleteReadSingle")
    public ReturnVO deleteReadSingle(@RequestParam("readType")Integer readType,@RequestParam("readTypeSub") Integer readTypeSub,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (readType==null||readTypeSub==null||id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        readService.createTable(readType,readTypeSub);
        dynamicTableNameUtil.SetTableName("read","_"+readType.toString()+"_"+readTypeSub.toString());
        boolean result = readService.removeById(id);
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
    @PostMapping("/deleteReadBatch")
    public ReturnVO deleteReadBatch(@RequestParam("ids") List<Integer> ids,@RequestParam("readType")Integer readType,@RequestParam("readTypeSub") Integer readTypeSub){
        returnVO = new ReturnVO();
        if (readType==null||readTypeSub==null||ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        readService.createTable(readType,readTypeSub);
        dynamicTableNameUtil.SetTableName("read","_"+readType.toString()+"_"+readTypeSub.toString());
        boolean result = readService.removeBatchByIds(ids);
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
    @PostMapping("/insert")
    public ReturnVO insert(@RequestParam("read")String readStr,@RequestParam("readType")Integer readType,@RequestParam("readTypeSub") Integer readTypeSub){
        returnVO =  new ReturnVO();
        if (readStr==null||readStr.equals("")||readType==null||readTypeSub==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Read read = (Read) jsonUtil.parseJsonStrToJavaObject(readStr,Read.class);
        if (read!=null){
            read.setUpdatetime(timeUtil.getNowLocalDateTime());
            read.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            read.setDeleted(0);
            readService.createTable(readType,readTypeSub);
            dynamicTableNameUtil.SetTableName("read","_"+readType.toString()+"_"+readTypeSub.toString());
            boolean result =  readService.save(read);
            if (result){

                returnVO.setCode(200);
                returnVO.setMessage("保存文章成功");
                return returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("保存文章失败2");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("保存文章失败1");
            return returnVO;
        }
    }
    @PostMapping("/update")
    public ReturnVO update(@RequestParam("read")String readStr,@RequestParam("readType")Integer readType,@RequestParam("readTypeSub") Integer readTypeSub){
        returnVO =  new ReturnVO();
        if (readStr==null||readStr.equals("")||readType==null||readTypeSub==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Read read = (Read) jsonUtil.parseJsonStrToJavaObject(readStr,Read.class);
        if (read!=null){
            read.setUpdatetime(timeUtil.getNowLocalDateTime());
            readService.createTable(readType,readTypeSub);
            dynamicTableNameUtil.SetTableName("read","_"+readType.toString()+"_"+readTypeSub.toString());
            boolean result =  readService.updateById(read);
            if (result){

                returnVO.setCode(200);
                returnVO.setMessage("更新成功");
                return returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("更新文章失败2");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("更新文章失败1");
            return returnVO;
        }

    }

    @PostMapping("/getReadTypeSubExist")
    public ReturnVO getReadTypeSubExist(@RequestParam("dsc")String dsc,@RequestParam("readType")Integer readType){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")||readType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        readtypesubService.createTable(readType);
        dynamicTableNameUtil.SetTableName("readtypesub","_"+readType);
        Readtypesub readtypesub = readtypesubService.getOne(new QueryWrapper<Readtypesub>().eq("dsc",dsc));
        if (readtypesub!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/deleteReadTypeSub")
    public ReturnVO deleteReadTypeSub(@RequestParam("id")Integer id,@RequestParam("readType")Integer readType){
        returnVO = new ReturnVO();
        if (id==null||id==1||readType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        readtypesubService.createTable(readType);
        dynamicTableNameUtil.SetTableName("readtypesub","_"+readType);
        boolean result = readtypesubService.removeById(id);
        if (result){
            readService.createTable(readType,id);
            dynamicTableNameUtil.SetTableName("read","_"+readType.toString()+"_"+id.toString());
            long count = readService.count();
            if (count!=0){
                readService.createTable(readType,id);
                dynamicTableNameUtil.SetTableName("read","_"+readType.toString()+"_"+id.toString());
                boolean result2 =readService.remove(new QueryWrapper<>());
                if (result2){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除read失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
                return returnVO;
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("删除readtypesub失败");
            return returnVO;
        }
    }

    @PostMapping("/insertReadTypeSub")
    public ReturnVO insertReadTypeSub(@RequestParam("readType")Integer readType,@RequestParam("readTypeSub")String readTypeSub){
        returnVO = new ReturnVO();
        if (readType==null||readTypeSub==null||readTypeSub.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Readtypesub readtypesub = (Readtypesub) jsonUtil.parseJsonStrToJavaObject2(readTypeSub,Readtypesub.class);
        if (readtypesub!=null){
            readtypesub.setCreatetime(timeUtil.getNowLocalDateTime());
            readtypesub.setDeleted(0);
            readtypesub.setId(null);
            readtypesubService.createTable(readType);
            dynamicTableNameUtil.SetTableName("readtypesub","_"+readType);
            boolean result = readtypesubService.save(readtypesub);
            if (result){
                readService.createTable(readType,readtypesub.getId());
                returnVO.setCode(200);
                returnVO.setMessage("添加成功");
                returnVO.setData(readtypesub);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("添加失败1");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败2");
            return returnVO;
        }

    }
    @PostMapping("/updateReadTypeSub")
    public ReturnVO updateReadTypeSub(@RequestParam("readType")Integer readType,@RequestParam("readTypeSub")String readTypeSub){
        returnVO = new ReturnVO();
        if (readType==null||readTypeSub==null||readTypeSub.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Readtypesub readtypesub = (Readtypesub) jsonUtil.parseJsonStrToJavaObject(readTypeSub,Readtypesub.class);
        if (readtypesub!=null){
            readtypesub.setUpdatetime(timeUtil.getNowLocalDateTime());
            readtypesub.setDeleted(0);
            readtypesubService.createTable(readType);
            dynamicTableNameUtil.SetTableName("readtypesub","_"+readType);
            boolean result = readtypesubService.updateById(readtypesub);
            if (result){
                readService.createTable(readType,readtypesub.getId());
                returnVO.setCode(200);
                returnVO.setMessage("修改成功");
                returnVO.setData(readtypesub);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("修改失败1");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改失败2");
            return returnVO;
        }

    }

    @PostMapping("/getReadTypeExist")
    public ReturnVO getReadTypeExist(@RequestParam("dsc")String dsc){
        returnVO = new ReturnVO();
        if (dsc==null||dsc.trim().equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Readtype readtype = readtypeService.getOne(new QueryWrapper<Readtype>().eq("dsc",dsc));
        if (readtype!=null){
            returnVO.setCode(200);
            returnVO.setMessage("存在");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("不存在");
            return returnVO;
        }
    }
    @PostMapping("/deleteReadType")
    public ReturnVO deleteReadType(@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (id==null||id==1){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        boolean result = readtypeService.removeById(id);
        if (result){
            readtypesubService.createTable(id);
            dynamicTableNameUtil.SetTableName("readtypesub","_"+id.toString());
            long count = readtypesubService.count();
            if (count!=0){
                readtypesubService.createTable(id);
                dynamicTableNameUtil.SetTableName("readtypesub","_"+id.toString());
                boolean result2 =readtypesubService.remove(new QueryWrapper<>());
                if (result2){
                    returnVO.setCode(200);
                    returnVO.setMessage("删除成功");
                    return returnVO;
                }else{
                    returnVO.setCode(500);
                    returnVO.setMessage("删除readtypesub失败");
                    return returnVO;
                }
            }else{
                returnVO.setCode(200);
                returnVO.setMessage("删除成功");
                return returnVO;
            }


        }else{
            returnVO.setCode(500);
            returnVO.setMessage("删除readtype失败");
            return returnVO;
        }
    }

    @PostMapping("/insertReadType")
    public ReturnVO insertReadType(@RequestBody Readtype readtype){
        returnVO = new ReturnVO();
        if (readtype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        readtype.setCreatetime(timeUtil.getNowLocalDateTime());
        readtype.setDeleted(0);
        boolean result = readtypeService.save(readtype);
        if (result){
            readtypesubService.createTable(readtype.getId());
            returnVO.setCode(200);
            returnVO.setMessage("添加成功");
            returnVO.setData(readtype);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("添加失败");
            return returnVO;
        }
    }
    @PostMapping("/updateReadType")
    public ReturnVO updateReadType(@RequestBody Readtype readtype){
        returnVO = new ReturnVO();
        if (readtype==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        readtype.setUpdatetime(timeUtil.getNowLocalDateTime());
        readtype.setDeleted(0);
        boolean result = readtypeService.updateById(readtype);
        if (result){
            readtypesubService.createTable(readtype.getId());
            returnVO.setCode(200);
            returnVO.setMessage("修改成功");
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("修改失败");
            return returnVO;
        }
    }

    @PostMapping("/getReadType")
    public  ReturnVO getReadType(){
        returnVO = new ReturnVO();
        List<Readtype> readtypes =readtypeService.list();
        if (readtypes!=null&&readtypes.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(readtypes);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/getReadTypeSub")
    public  ReturnVO getReadTypeSub(@RequestParam("readType")Integer readType){
        returnVO = new ReturnVO();
        if (readType==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        readtypesubService.createTable(readType);
        dynamicTableNameUtil.SetTableName("readtypesub","_"+readType);
        List<Readtypesub> readtypes = readtypesubService.list();
        if (readtypes!=null&&readtypes.size()!=0){
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(readtypes);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
            return returnVO;
        }
    }

    @PostMapping("/getOneById")
    public  ReturnVO getOneById(@RequestParam("readType")Integer readType,@RequestParam("readTypeSub")Integer readTypeSub,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (readType==null||readTypeSub==null||id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }


        readService.createTable(readType,readTypeSub);
        dynamicTableNameUtil.SetTableName("read","_"+readType+"_"+readTypeSub);
        Read read = readService.getById(id);
        if (read!=null){

            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData( read);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;
    }

    @PostMapping("/getRead")
    public  ReturnVO getRead(@RequestBody ReadCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Read> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());
        readService.createTable(condition.getReadType(),condition.getReadTypeSub());
        dynamicTableNameUtil.SetTableName("read","_"+condition.getReadType().toString()+"_"+condition.getReadTypeSub().toString());
        IPage<Read> iPage = readService.page(page,getConditionWrapper(new QueryWrapper<Read>(),condition));
        if (iPage.getRecords()!=null){
            List<Read> reads =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Read>) iPage.getRecords();
            ArrayList<ReadFront> readFronts = new ArrayList<>();
            for (Read read:reads
            ) {
                List<String> sentences = wordUtil.spilitSentence(base64Util.decodeToString(read.getEssay()));
                ReadFront readFront = new ReadFront();
                readFront.setId(read.getId());
                readFront.setBrief(read.getBrief());
                readFront.setAuthor(read.getAuthor());
                readFront.setImage(read.getImage());
                readFront.setDeleted(read.getDeleted());
                readFront.setCreatetime(read.getCreatetime());
                readFront.setName(read.getName());
                readFront.setSentences(sentences);
                readFront.setUpdatetime(read.getUpdatetime());
                readFronts.add(readFront);
            }
            MyPage<ReadFront> myPage = new MyPage<>();
            myPage.setData(readFronts);
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

    private QueryWrapper<Read> getConditionWrapper(QueryWrapper<Read> wrapper, ReadCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getEssaySearch()!=null){
            wrapper = wrapper.like("essay",condition.getEssaySearch());
        }
        if (condition.getNameSearch()!=null){
            wrapper = wrapper.like("name",condition.getNameSearch());
        }
        if (condition.getAuthorSearch()!=null){
            wrapper = wrapper.like("author",condition.getAuthorSearch());
        }
        if (condition.getBriefSearch()!=null){
            wrapper = wrapper.like("brief",condition.getBriefSearch());
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
