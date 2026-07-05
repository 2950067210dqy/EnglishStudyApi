package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFull;

import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFullParent;
import com.dqy.englishstudyapi.service.FreshwordService;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Freshword;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-16
 */
@RestController
@RequestMapping("freshword")
public class FreshwordController {
    @Autowired
    FreshwordService freshwordService;
    @Autowired
    WordService wordService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    ListUtil listUtil;
    ReturnVO returnVO;


    @RequestMapping(value = "/getAllEq",method = RequestMethod.POST)
    public  ReturnVO  getAllEq(@RequestParam("uid")Integer uid,@RequestParam("data")String data) {
        returnVO = new ReturnVO();
        ArrayList<WordSimpleEnd> wordSimpleEnds = new ArrayList<>();
        String[] datas = data.split(",");
        if (data == null || uid == null || data.equals("")) {
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        } else {
            for (String d : datas
            ) {
                WordSimpleEnd wordSimpleEnd = new WordSimpleEnd();
                wordSimpleEnd.setId(Integer.valueOf(d.substring(1)));
                wordSimpleEnd.setInitial(d.substring(0, 1));
                wordSimpleEnds.add(wordSimpleEnd);
            }
            if (wordSimpleEnds != null && wordSimpleEnds.size() != 0) {
                Freshword freshword = freshwordService.getOne(new QueryWrapper<Freshword>().eq("uid", uid));
                if (freshword != null) {
                    ArrayList<WordSimpleEnd> wordSimpleEndsStore = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(), WordSimpleEnd.class);
                    boolean flag = false;
                    Iterator<WordSimpleEnd> wordSimpleEndIterator = wordSimpleEnds.iterator();
                    while (wordSimpleEndIterator.hasNext()) {
                        WordSimpleEnd temp = wordSimpleEndIterator.next();

                        for (WordSimpleEnd w : wordSimpleEndsStore
                        ) {
                            if (Objects.equals( temp.getId(),w.getId()) &&w.getInitial().equals(temp.getInitial())) {

                                flag=true;

                            }
                        }
                        if (!flag){
                            flag=true;
                            break;
                        }else{
                            flag=false;
                        }
                    }

                    if (flag) {
                        returnVO.setMessage("存在不相同");
                        returnVO.setCode(200);
                        return returnVO;
                    } else {
                        returnVO.setMessage("存在所有相同");
                        returnVO.setCode(500);
                        return returnVO;
                    }
                } else {
                    freshword = new Freshword();
                    freshword.setCreatetime(timeUtil.getNowLocalDateTime());
                    freshword.setUid(uid);
                    freshword.setDeleted(0);
                    freshword.setWords("");
                    boolean result = freshwordService.save(freshword);
                    if (result) {
                        returnVO.setMessage("创建生词本成功");
                        returnVO.setCode(200);
                        return returnVO;
                    } else {
                        returnVO.setMessage("创建生词本失败");
                        returnVO.setCode(200);
                        return returnVO;
                    }
                }
            } else {
                returnVO.setMessage("数据切片无单词");
                returnVO.setCode(200);
                return returnVO;
            }
        }
    }

    @RequestMapping(value = "insertBatchByUid",method = RequestMethod.POST)
    public  ReturnVO  insertBatchByUid(@RequestParam("uid")Integer uid,@RequestParam("data")String data){
        returnVO = new ReturnVO();
        ArrayList<WordSimpleEnd> wordSimpleEnds = new ArrayList<>();
        String[] datas = data.split(",");
        if ( data == null || uid == null||data.equals("")) {
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        } else {
            for (String d:datas
            ) {
                WordSimpleEnd wordSimpleEnd = new WordSimpleEnd();
                wordSimpleEnd.setId(Integer.valueOf(d.substring(1)));
                wordSimpleEnd.setInitial(d.substring(0,1));
                wordSimpleEnds.add(wordSimpleEnd);
            }
            if (wordSimpleEnds!=null&&wordSimpleEnds.size()!=0){
                Freshword freshword = freshwordService.getOne(new QueryWrapper<Freshword>().eq("uid",uid));
                if (freshword!=null){
                    ArrayList<WordSimpleEnd> wordSimpleEndsStore =jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
                    boolean flag =true;
                    Iterator<WordSimpleEnd> wordSimpleEndIterator = wordSimpleEnds.iterator();
                    while (wordSimpleEndIterator.hasNext()){
                        WordSimpleEnd temp = wordSimpleEndIterator.next();
                        for (WordSimpleEnd w:wordSimpleEndsStore
                        ) {
                            if (Objects.equals(temp.getId(),w.getId())&&w.getInitial().equals(temp.getInitial())){
                                wordSimpleEndIterator.remove();
                            }
                        }
                    }

                    wordSimpleEndsStore.addAll(wordSimpleEnds);

                    freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEndsStore));
                    boolean result = freshwordService.updateById(freshword);
                    if (result){
                        returnVO.setMessage("添加成功");
                        returnVO.setCode(200);
                        return returnVO;
                    }else{
                        returnVO.setMessage("添加失败");
                        returnVO.setCode(500);
                        return returnVO;
                    }
                }else{
                    freshword = new Freshword();
                    freshword.setCreatetime(timeUtil.getNowLocalDateTime());
                    freshword.setUid(uid);
                    freshword.setDeleted(0);
                    freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEnds));
                    boolean result = freshwordService.save(freshword);
                    if (result){
                        returnVO.setMessage("添加成功");
                        returnVO.setCode(200);
                        return returnVO;
                    }else{
                        returnVO.setMessage("添加失败");
                        returnVO.setCode(500);
                        return returnVO;
                    }
                }
            }else{
                returnVO.setMessage("数据切片无单词");
                returnVO.setCode(500);
                return returnVO;
            }
        }



    }
    @RequestMapping(value = "deleteBatchByUid",method = RequestMethod.POST)
    public  ReturnVO  deleteBatchByUid(@RequestParam("uid")Integer uid,@RequestParam("data")String data){
        returnVO = new ReturnVO();
        ArrayList<WordSimpleEnd> wordSimpleEnds = new ArrayList<>();
        String[] datas = data.split(",");
        if ( data == null || uid == null||data.equals("")) {
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return returnVO;
        } else {
            for (String d:datas
            ) {
                WordSimpleEnd wordSimpleEnd = new WordSimpleEnd();
                wordSimpleEnd.setId(Integer.valueOf(d.substring(1)));
                wordSimpleEnd.setInitial(d.substring(0,1));
                wordSimpleEnds.add(wordSimpleEnd);
            }
            if (wordSimpleEnds!=null&&wordSimpleEnds.size()!=0){
                Freshword freshword = freshwordService.getOne(new QueryWrapper<Freshword>().eq("uid",uid));
                if (freshword!=null){
                    ArrayList<WordSimpleEnd> wordSimpleEndsStore =jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
                    boolean flag =false;
                    Iterator<WordSimpleEnd> wordi = wordSimpleEndsStore.iterator();
                    while (wordi.hasNext()){
                        WordSimpleEnd TEMP =wordi.next();
                        for (WordSimpleEnd w:wordSimpleEnds
                        ) {
                            if (Objects.equals(TEMP.getId(),w.getId())&&w.getInitial().equals(TEMP.getInitial())){
                                wordi.remove();
                            }
                        }
                    }

                    freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEndsStore));
                    boolean result = freshwordService.updateById(freshword);
                    if (result){
                        returnVO.setMessage("删除成功");
                        returnVO.setCode(200);
                        return returnVO;
                    }else{
                        returnVO.setMessage("删除失败");
                        returnVO.setCode(500);
                        return returnVO;
                    }
                }else{
                    freshword = new Freshword();
                    freshword.setCreatetime(timeUtil.getNowLocalDateTime());
                    freshword.setUid(uid);
                    freshword.setDeleted(0);
                    freshword.setWords("");
                    boolean result = freshwordService.save(freshword);
                    if (result){
                        returnVO.setMessage("创建生词本成功");
                        returnVO.setCode(500);
                        return returnVO;
                    }else{
                        returnVO.setMessage("创建生词本失败");
                        returnVO.setCode(500);
                        return returnVO;
                    }
                }
            }else{
                returnVO.setMessage("数据切片无单词");
                returnVO.setCode(500);
                return returnVO;
            }
        }




    }
    @RequestMapping(value = "insertOneByUid",method = RequestMethod.POST)
    public  ReturnVO  insertOneByUid(@RequestParam("uid")Integer uid,@RequestParam("wid")Integer wid,@RequestParam("initial")String initial){
        returnVO = new ReturnVO();
        Freshword freshword = freshwordService.getOne(new QueryWrapper<Freshword>().eq("uid",uid));
        if (freshword!=null){
            ArrayList<WordSimpleEnd> wordSimpleEnds =jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
            boolean flag =true;
            if (wordSimpleEnds==null){
                wordSimpleEnds = new ArrayList<>();
            }
                for (WordSimpleEnd w:wordSimpleEnds
                ) {
                    if (Objects.equals(wid,w.getId())&&w.getInitial().equals(initial)){
                        flag=false;
                        break;
                    }
                }

            if (flag){
                WordSimpleEnd wordSimpleEnd = new WordSimpleEnd();
                wordSimpleEnd.setId(wid);
                wordSimpleEnd.setInitial(initial);
                wordSimpleEnds.add(wordSimpleEnd);
                freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEnds));
                boolean result = freshwordService.updateById(freshword);
                if (result){
                    returnVO.setMessage("添加成功");
                    returnVO.setCode(200);
                    return returnVO;
                }else{
                    returnVO.setMessage("添加失败");
                    returnVO.setCode(500);
                    return returnVO;
                }
            }
            returnVO.setMessage("添加成功");
            returnVO.setCode(200);
            return returnVO;

        }else{
            freshword = new Freshword();
            freshword.setUpdatetime(timeUtil.getNowLocalDateTime());
            freshword.setUid(uid);
            freshword.setCreatetime(timeUtil.getNowLocalDateTime());
            freshword.setDeleted(0);
            ArrayList<WordSimpleEnd> wordSimpleEnds = new ArrayList<>();
            WordSimpleEnd wordSimpleEnd = new WordSimpleEnd();
            wordSimpleEnd.setInitial(initial);
            wordSimpleEnd.setId(wid);
            wordSimpleEnds.add(wordSimpleEnd);
            freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEnds));
            boolean result = freshwordService.save(freshword);
            if (result){
                returnVO.setMessage("保存成功");
                returnVO.setCode(200);
                return returnVO;
            }else{
                returnVO.setMessage("保存失败");
                returnVO.setCode(500);
                return returnVO;
            }

        }

    }
    @RequestMapping(value = "deleteOneByUid",method = RequestMethod.POST)
    public  ReturnVO  deleteOneByUid(@RequestParam("uid")Integer uid,@RequestParam("wid")Integer wid,@RequestParam("initial")String initial){
        returnVO = new ReturnVO();
        Freshword freshword = freshwordService.getOne(new QueryWrapper<Freshword>().eq("uid",uid));
        if (freshword!=null){
            ArrayList<WordSimpleEnd> wordSimpleEnds =jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
            boolean flag =false;
            Iterator<WordSimpleEnd> wordi = wordSimpleEnds.iterator();
            while (wordi.hasNext()){
                WordSimpleEnd TEMP =wordi.next();

                if (Objects.equals(TEMP.getId(),wid)&&TEMP.getInitial().equals(initial)){
                    flag=true;
                    wordi.remove();
                    break;
                }
            }
            if (flag){
                freshword.setWords(jsonUtil.parseArrayListToJsonStrThenToBase64(wordSimpleEnds));
                boolean result = freshwordService.updateById(freshword);
                if (result){
                    returnVO.setMessage("删除成功");
                    returnVO.setCode(200);
                    return returnVO;
                }else{
                    returnVO.setMessage("删除失败");
                    returnVO.setCode(500);
                    return returnVO;
                }
            }
            returnVO.setMessage("删除失败，生词本未有该单词");
            returnVO.setCode(500);
            return returnVO;
        }else{
            returnVO.setMessage("查找不到");
            returnVO.setCode(500);
            return returnVO;
        }

    }
    @RequestMapping(value = "/getByUid",method = RequestMethod.POST)
    public ReturnVO getByUid(@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }
        Freshword freshword = freshwordService.getOne(new QueryWrapper<Freshword>().eq("uid",uid));
       WordFullParent wordFullParent = new WordFullParent();
        if (freshword==null){
            freshword = new Freshword();
            freshword.setUid(uid);
            freshword.setDeleted(0);
            freshword.setWords("");
            freshword.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            boolean result = freshwordService.save(freshword);
            if (result){
               wordFullParent.setCount(0);
               returnVO.setMessage("查询成功");
               returnVO.setCode(200);
               returnVO.setData(wordFullParent);
//               returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordFullForIndexListParent));
               return  returnVO;
            }else{
                returnVO.setMessage("插入失败");
                returnVO.setCode(500);
                return  returnVO;
            }
        }else{
            ArrayList<WordSimpleEnd> words =jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
            if (words!=null&&words.size()!=0){
                final Integer[] count = {0};
                HashMap<Character,ArrayList<WordSimpleEnd>>  classfiedWordSimpleEnd = listUtil.wordSimpleEndListClassifyByInitial(words);
                ArrayList<WordFull> wordFulls = new ArrayList<>();
                classfiedWordSimpleEnd.keySet().forEach(key ->{
                    WordFull wordFull = new WordFull();
                    wordFull.setInitial(String.valueOf(key));
                    ArrayList<WordSimpleEnd> wordSimpleEndS = classfiedWordSimpleEnd.get(key);
                    if (wordSimpleEndS.size()!=0){
                        //导入单词 进入单词service
                        ArrayList<Word> wordArrayList= wordService.selectByIdBatch2(String.valueOf(key), wordSimpleEndS);
                        ArrayList<WordEnd> datas = new ArrayList<>();
                        if (wordArrayList!=null&&wordArrayList.size()!=0){
                            for (Word w:wordArrayList
                            ) {
                                WordEnd wordEnd = new WordEnd();
                                wordEnd.setWord(w);
                                wordEnd.setInBook(true);
                                datas.add(wordEnd);
                            }
                            count[0] +=wordArrayList.size();
                            wordFull.setCount(wordArrayList.size());
                            wordFull.setWords(datas);
                        }else{
                            count[0] +=0;


                            wordFull.setCount(0);
                            wordFull.setWords(datas);
                        }



                    }
                    wordFulls.add(wordFull);
                });
                wordFullParent.setWordFulls(wordFulls);


                wordFullParent.setCount(count[0]);
                wordFullParent.setFreshword(freshword);
                returnVO.setMessage("获取单词成功");
                returnVO.setCode(200);
//                returnVO.setData(wordFullParent);
                returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64( wordFullParent));
                return  returnVO;
            }else{
                wordFullParent.setCount(0);
                returnVO.setMessage("查询成功");
                returnVO.setCode(200);
//                returnVO.setData(wordFullParent);
               returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordFullParent));
                return  returnVO;
            }
        }
    }
}
