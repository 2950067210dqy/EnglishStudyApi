package com.dqy.englishstudyapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.FreshwordService;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.DynamicTableNameUtil;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.util.RandomUtil;
import com.dqy.englishstudyapi.util.WordUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@RestController
@RequestMapping("word")
public class WordController {
    @Autowired
    WordService wordService;
    @Autowired
    FreshwordService freshwordService;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    RandomUtil randomUtil;
    @Autowired
    ListUtil listUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    ReturnVO returnVO;
    //获取单个单词
    @RequestMapping(value = "/getOneOpen",method = RequestMethod.POST)
    public ReturnVO getOneOpen(@RequestParam("word") String word ,@RequestParam(value = "type",required = false,defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        if (wordUtil.isContainChinese(word)) {
            word = wordUtil.replaceEnglish(word);
            word = wordUtil.replaceNumber(word);
            word = wordUtil.filterNotation(word);
            word=wordUtil.filter(word);

            ArrayList<Word> words =wordService.selectAllByTrans(word);
            if (words!=null&&words.size()!=0){
                returnVO.setCode(200);
                returnVO.setMessage("查找成功");
                returnVO.setData(words);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("查不到这个单词");
                return  returnVO;
            }
        }else{
            word = wordUtil.replaceNumber(word);
            word = wordUtil.replaceChinese(word);
            word = wordUtil.filterNotation(word);
            word=wordUtil.filter(word);
            String initial=String.valueOf(word.charAt(0));
            switch (initial){
                case "0":
                    initial="z";
                    break;
                case "1":initial="o";break;
                case "2":initial="t";break;
                case "3":initial="t";break;
                case "4":initial="f";break;
                case "5":initial="f";break;
                case "6":initial="s";break;
                case "7":initial="s";break;
                case "8":initial="e";break;
                case "9":initial="n";break;
                default:initial=initial.toLowerCase();break;
            }

            ArrayList<Word> words =new ArrayList<>();
            if (type==0){
                Word word1 = wordService.selectByWord(initial,word);
                words.add(word1);
            }else if(type==1){
                words= wordService.SelectLikeRightByWord(initial,word);
            }else if(type==2){
                words= wordService.SelectLikeLeftByWord(initial,word);
            }else{
                words= wordService.SelectLikeByWord(initial,word);
            }
            if (words!=null&&words.size()!=0&&words.get(0)!=null){

                returnVO.setCode(200);
                returnVO.setMessage("查找成功");
                returnVO.setData(words);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("查不到这个单词");
                return  returnVO;
            }
        }


    }
    //获取单个单词
    @RequestMapping(value = "/getOne",method = RequestMethod.POST)
    public ReturnVO getOne(@RequestParam("word") String word , @RequestParam("uid")Integer uid,@RequestParam(value = "type",required = false,defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        word=wordUtil.filter(word);
        String initial=String.valueOf(word.charAt(0));
        switch (initial){
            case "0":
                initial="z";
                break;
            case "1":initial="o";break;
            case "2":initial="t";break;
            case "3":initial="t";break;
            case "4":initial="f";break;
            case "5":initial="f";break;
            case "6":initial="s";break;
            case "7":initial="s";break;
            case "8":initial="e";break;
            case "9":initial="n";break;
            default:initial=initial.toLowerCase();break;
        }

        ArrayList<Word> words =new ArrayList<>();
        if (type==0){
             Word word1 = wordService.selectByWord(initial,word);
             words.add(word1);
        }else if(type==1){
            words= wordService.SelectLikeRightByWord(initial,word);
        }else if(type==2){
            words= wordService.SelectLikeLeftByWord(initial,word);
        }else{
            words= wordService.SelectLikeByWord(initial,word);
        }
        if (words!=null&&words.size()!=0&&words.get(0)!=null){
            ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
            List<WordEnd> wordEnds = new ArrayList<>();
            wordEnds=freshwordService.handleWords(words,exitsWordArrayList);
            if (wordEnds.size()>10){
                wordEnds=wordEnds.subList(0,10);
            }
            returnVO.setCode(200);
            returnVO.setMessage("查找成功");
            returnVO.setData(wordEnds);
            return returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("查不到这个单词");
            return  returnVO;
        }

    }


    //获取随机的一个单词
    @RequestMapping(value = "/getRandomOne",method = RequestMethod.POST)
    public ReturnVO getRandomOne(){
        returnVO = new ReturnVO();
       Integer initialRandom =  randomUtil.getRandomRange(0,listUtil.abs.length-1);
       if (initialRandom<0||initialRandom>listUtil.abs.length-1){
           returnVO.setCode(500);
           returnVO.setMessage("随机数initialRandom错误");
           return returnVO;
       }
        Character initial = listUtil.abs[initialRandom];
        dynamicTableNameUtil.SetTableName("word","_"+initial.toString());

        Long count = wordService.count();
        if (count!=null&&count!=0L){
            Integer random =  randomUtil.getRandomRange(0, Math.toIntExact(count)-1);
            if (random<0||random>count-1L){
                returnVO.setCode(500);
                returnVO.setMessage("随机数random错误");
                return returnVO;
            }
            Word word = wordService.getOne(new QueryWrapper<Word>().last(" limit "+random+",1 "));
            if (word!=null){
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(word);
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("获取单词错误");
                return returnVO;
            }

        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取单词库错误或为空");
            return returnVO;
        }



    }
}
