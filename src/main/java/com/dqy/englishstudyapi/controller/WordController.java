package com.dqy.englishstudyapi.controller;


import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.FreshwordService;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.WordUtil;
import com.dqy.englishstudyapi.vo.ReturnVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    ReturnVO returnVO;
    //获取单个单词
    @RequestMapping(value = "/getOne",method = RequestMethod.POST)
    public ReturnVO getOne(@RequestParam("word") String word , @RequestParam("uid")Integer uid,@RequestParam(value = "type",required = false,defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        word=wordUtil.filterNotation(word);
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
        }else{
            words= wordService.SelectLikeRightByWord(initial,word);
        }
        if (words!=null&&words.size()!=0){
            ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
           ArrayList<WordEnd> wordEnds = new ArrayList<>();
            wordEnds=freshwordService.handleWords(words,exitsWordArrayList);
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
}
