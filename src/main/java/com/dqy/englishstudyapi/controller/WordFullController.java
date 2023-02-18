package com.dqy.englishstudyapi.controller;

import com.dqy.englishstudyapi.entity.endEntity.ReviewEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.FrontLiJu;
import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFull;
import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFullParent;
import com.dqy.englishstudyapi.entity.frontEntity.WordLearn.WordLearn;
import com.dqy.englishstudyapi.entity.frontEntity.WordLearn.WordLearnSubWord;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.*;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("wordFull")
public class WordFullController {
    ReturnVO returnVO;
    @Autowired
    CikuexampleService cikuexampleService;
    @Autowired
    CikuService cikuService;
    @Autowired
    WordService wordService;
    @Autowired
    ResiteService resiteService;
    @Autowired
    NowresiteService nowresiteService;
    @Autowired
    FreshwordService freshwordService;
    @Autowired
    LijuService lijuService;
    @Autowired
    ListUtil listUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    Base64Util base64Util;
    @Autowired
    RandomUtil randomUtil;
    @Autowired
    TimeUtil timeUtil;

    //获取词库所有单词
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public ReturnVO get(@RequestParam(value = "cikuTypeId")Integer cikuTypeId,@RequestParam(value = "cikuId")Integer cikuId ,@RequestParam("uid")Integer uid){
        returnVO = new ReturnVO();
        if (cikuTypeId==null||cikuId==null||uid==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据不为空");
            return  returnVO ;
        }else{
            WordFullParent wordFullParent = new WordFullParent();
            ArrayList<Cikuexample> cikuexamples =cikuexampleService.list(cikuTypeId,cikuId);
            HashMap<Character,ArrayList<Cikuexample>>  classfiedCikuexamples = listUtil.cikuexampleListClassifyByInitial(cikuexamples);
            ArrayList<WordFull> wordFulls = new ArrayList<>();
            classfiedCikuexamples.keySet().forEach(key ->{
                WordFull wordFull = new WordFull();
                wordFull.setInitial(String.valueOf(key));
                ArrayList<Cikuexample> cikuexampleArrayList = classfiedCikuexamples.get(key);
                if (cikuexampleArrayList.size()!=0){
                    //导入单词 进入单词service
                    ArrayList<Word> wordArrayList= wordService.selectByIdBatch(String.valueOf(key), cikuexampleArrayList);
                    ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
                    ArrayList<WordEnd> datas = new ArrayList<>();
                    datas=freshwordService.handleWords(wordArrayList,exitsWordArrayList);
                    wordFull.setWords(datas);

                }
                wordFulls.add(wordFull);
            });
            wordFullParent.setWordFulls(wordFulls);
            Ciku ciku = cikuService.selectById(cikuTypeId,cikuId);
            wordFullParent.setCiku(ciku);
            Integer count =cikuexampleService.count(cikuTypeId,cikuId);
            wordFullParent.setCount(count);
            returnVO.setMessage("获取单词成功");
            returnVO.setCode(200);
            returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordFullParent));
            return  returnVO;
        }


    }

//获取复习单词
    @RequestMapping(value = "/getReview",method = RequestMethod.POST)
    public ReturnVO getReview(@RequestParam(value = "reciteid")Integer reciteId,@RequestParam(value = "num",defaultValue = "20")Integer num,@RequestParam(value = "uid")Integer uid,@RequestParam(value = "type",defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        if (reciteId==null||num==null){
            returnVO.setMessage("数据不能为空");
            returnVO.setCode(500);
            return returnVO;

        }else{
            WordLearn wordLearn = new WordLearn();

            Resite resite = resiteService.getById(reciteId);
            if (resite!=null){
                if (type==0){
                    //词库
                    String review1 =base64Util.decodeToString(resite.getReview1());
                    String review2 =base64Util.decodeToString(resite.getReview2());
                    String review4 =base64Util.decodeToString(resite.getReview4());
                    String review7 =base64Util.decodeToString(resite.getReview7());
                    String review15 =base64Util.decodeToString(resite.getReview15());
                    String over =base64Util.decodeToString(resite.getOver());
                    Long differDay = timeUtil.differDay(resite.getCreatetime(),timeUtil.getNowLocalDateTime());
                    HashMap<String, ArrayList<Integer>> wordsMap = new HashMap<>();

                    System.out.println("differDay:"+differDay);
                    if (differDay>=1L&&differDay<2L){
                        if (!("".equals(review1))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("1",words);
                        }
                    }
                    else if(differDay>=2L&&differDay<4L){
                        if (!("".equals(review1))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("2",words);
                        }
                    }
                    else if(differDay>=4L&&differDay<7L){
                        if (!("".equals(review1))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("2",words);
                        }
                        if (!("".equals(review4))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("4",words);
                        }

                    }
                    else if(differDay>=7L&&differDay<15L){
                        if (!("".equals(review1))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("2",words);
                        }
                        if (!("".equals(review4))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("4",words);
                        }
                        if (!("".equals(review7))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("7",words);
                        }
                    }
                    else if(differDay>=15){
                        if (!("".equals(review1))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("2",words);
                        }
                        if (!("".equals(review4))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("4",words);
                        }
                        if (!("".equals(review7))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("7",words);
                        }
                        if (!("".equals(review15))){
                            ArrayList<Integer> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review15,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd.getId());
                                }
                            }
                            wordsMap.put("15",words);
                        }
                    }
                    if (wordsMap.containsKey("1")||wordsMap.containsKey("2")||wordsMap.containsKey("4")||wordsMap.containsKey("7")||wordsMap.containsKey("15")){
                        final int[] counts = {0};
                        ArrayList< WordLearnSubWord> wordLearnSubWords = new ArrayList<>();
                        wordsMap.keySet().forEach(key1 -> {
                            ArrayList<Integer> words = wordsMap.get(key1);

                            if (counts[0] <num&&words.size()!=0){
                                Integer[] cikuexampleids ;
                                if (counts[0] +words.size()<=num){
                                    cikuexampleids= new Integer[words.size()];
                                }else{
                                    if (words.size()>num){
                                        cikuexampleids= new Integer[num];
                                    }else{
                                        cikuexampleids= new Integer[counts[0] +words.size()-num];
                                    }

                                }

                                for (int i = 0; i <cikuexampleids.length ; i++) {
                                    cikuexampleids[i]=words.get(i);
                                    counts[0]++;
                                }

                                Integer nowresiteid = resite.getNowresiteid();
                                Nowresite nowresite =nowresiteService.getById(nowresiteid);
                                if (nowresite!=null){
                                    ArrayList<Cikuexample> cikuexamples =  cikuexampleService.listByIds(nowresite.getCikutypeid(),nowresite.getCikuid(),cikuexampleids);
                                    HashMap<Character,ArrayList<Cikuexample>>  classfiedCikuexamples = listUtil.cikuexampleListClassifyByInitial(cikuexamples);

                                    classfiedCikuexamples.keySet().forEach(key ->{
                                        ArrayList<Cikuexample> cikuexampleArrayList = classfiedCikuexamples.get(key);
                                        if (cikuexampleArrayList.size()!=0){

                                            ArrayList<Word> wordArrayList= wordService.selectByIdBatch(String.valueOf(key), cikuexampleArrayList);
                                            Integer index = 0;
                                            if (wordArrayList!=null){
                                                for (Word w:wordArrayList
                                                ) {
                                                    WordLearnSubWord wordLearnSubWord = new WordLearnSubWord();
                                                    Liju liju=   lijuService.getByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                    ArrayList<FrontLiJu> frontLiJus = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(liju.getSentences(),FrontLiJu.class) ;
                                                    ArrayList<Word> cizus =  wordService.getCizuByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                    Integer wordsCount = wordService.count(String.valueOf(w.getWord().charAt(0)).toLowerCase());
                                                    ArrayList<Word> choose = new ArrayList<>();
                                                    for (int i = 0; i <3 ; i++) {
                                                        Integer random =randomUtil.getRandomRange(0,wordsCount);
                                                        Word word = wordService.getRandom(String.valueOf(w.getWord().charAt(0)).toLowerCase(),random,1);
                                                        choose.add(word);

                                                    }
                                                    Word chooseSelf = new Word();
                                                    chooseSelf.setWord(w.getWord());
                                                    chooseSelf.setCreatetime(w.getCreatetime());
                                                    chooseSelf.setCharac(w.getCharac());
                                                    chooseSelf.setSoundmark1(w.getSoundmark1());
                                                    chooseSelf.setSoundmark2(w.getSoundmark2());
                                                    chooseSelf.setTrans(w.getTrans());
                                                    chooseSelf.setDeleted(w.getDeleted());
                                                    chooseSelf.setId(w.getId());
                                                    chooseSelf.setUpdatetime(w.getUpdatetime());
                                                    choose.add(chooseSelf);
                                                    Collections.shuffle(choose);


                                                    ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
                                                    wordLearnSubWord.setInBook(freshwordService.handleWordBoolean(w,exitsWordArrayList));

                                                    wordLearnSubWord.setType(key1);
                                                    wordLearnSubWord.setChoose(choose);
                                                    wordLearnSubWord.setWord(w);
                                                    wordLearnSubWord.setCikuexampleid(cikuexampleArrayList.get(index).getId());
                                                    wordLearnSubWord.setCizu(cizus);
                                                    wordLearnSubWord.setLiju(frontLiJus);
                                                    wordLearnSubWords.add(wordLearnSubWord);
                                                    index++;
                                                }
                                            }

                                        }

                                    });
                                }else{
//                            returnVO.setMessage("获取nowrecite失败");
//                            returnVO.setCode(500);

                                }
                            }


                        });
                        wordLearn.setType(0);
                        wordLearn.setWords(wordLearnSubWords);
                        wordLearn.setNum(wordLearnSubWords.size());
                        returnVO.setMessage("获取成功");
                        returnVO.setCode(200);
                        //                   returnVO.setData(wordLearn);
                        returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordLearn));
                        return  returnVO;
                    }else{
                        returnVO.setMessage("已经背完了");
                        returnVO.setCode(200);
                    }
                }else{
                    //生词本
                    String review1 =base64Util.decodeToString(resite.getReview1());
                    String review2 =base64Util.decodeToString(resite.getReview2());
                    String review4 =base64Util.decodeToString(resite.getReview4());
                    String review7 =base64Util.decodeToString(resite.getReview7());
                    String review15 =base64Util.decodeToString(resite.getReview15());
                    String over =base64Util.decodeToString(resite.getOver());
                    Long differDay = timeUtil.differDay(resite.getCreatetime(),timeUtil.getNowLocalDateTime());
                    HashMap<String, ArrayList<ReviewEnd>> wordsMap = new HashMap<>();

                    System.out.println("differDay:"+differDay);
                    if (differDay>=1L&&differDay<2L){
                        if (!("".equals(review1))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("1",words);
                        }
                    }
                    else if(differDay>=2L&&differDay<4L){
                        if (!("".equals(review1))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("2",words);
                        }
                    }
                    else if(differDay>=4L&&differDay<7L){
                        if (!("".equals(review1))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("2",words);
                        }
                        if (!("".equals(review4))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("4",words);
                        }

                    }
                    else if(differDay>=7L&&differDay<15L){
                        if (!("".equals(review1))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("2",words);
                        }
                        if (!("".equals(review4))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("4",words);
                        }
                        if (!("".equals(review7))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("7",words);
                        }
                    }
                    else if(differDay>=15){
                        if (!("".equals(review1))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review1,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("1",words);
                        }
                        if (!("".equals(review2))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review2,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("2",words);
                        }
                        if (!("".equals(review4))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review4,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("4",words);
                        }
                        if (!("".equals(review7))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review7,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("7",words);
                        }
                        if (!("".equals(review15))){
                            ArrayList<ReviewEnd> words = new ArrayList<>();
                            ArrayList<ReviewEnd> reviewEnds = jsonUtil.parseJsonStrToArrayList(review15,ReviewEnd.class);
                            for (ReviewEnd reviewEnd:reviewEnds
                            ) {
                                Long differ = timeUtil.differDay(reviewEnd.getUpdatetime(),timeUtil.getNowLocalDateTime());
                                if (differ>=1L){
                                    words.add(reviewEnd);
                                }
                            }
                            wordsMap.put("15",words);
                        }
                    }
                    if (wordsMap.containsKey("1")||wordsMap.containsKey("2")||wordsMap.containsKey("4")||wordsMap.containsKey("7")||wordsMap.containsKey("15")){
                        final int[] counts = {0};
                        ArrayList< WordLearnSubWord> wordLearnSubWords = new ArrayList<>();
                        wordsMap.keySet().forEach(key1 -> {
                            ArrayList<ReviewEnd> words = wordsMap.get(key1);
                            ArrayList<WordSimpleEnd> wordss = new ArrayList<>();
                            for (ReviewEnd reviewEnd:words
                                 ) {
                                WordSimpleEnd wordSimpleEnd = new WordSimpleEnd();
                                wordSimpleEnd.setInitial(reviewEnd.getInitial());
                                wordSimpleEnd.setId(reviewEnd.getId());
                                wordss.add(wordSimpleEnd);
                            }
                            if (counts[0] <num&&words.size()!=0){


                                Integer nowresiteid = resite.getNowresiteid();
                                Nowresite nowresite =nowresiteService.getById(nowresiteid);
                                if (counts[0] <num&&nowresite!=null){

                                    HashMap<Character,ArrayList<WordSimpleEnd>> wordSimpleEndListClassifyByInitial = listUtil.wordSimpleEndListClassifyByInitial(wordss );

                                    wordSimpleEndListClassifyByInitial.keySet().forEach(key ->{
                                        ArrayList<WordSimpleEnd> wordSimpleEndListClassifyByInitialArrayList = wordSimpleEndListClassifyByInitial.get(key);
                                        if (counts[0] <num&&wordSimpleEndListClassifyByInitial.size()!=0){

                                            ArrayList<Word> wordArrayList= wordService.selectByIdBatch2(String.valueOf(key),wordSimpleEndListClassifyByInitialArrayList);
                                            Integer index = 0;
                                            if (counts[0] <num&&wordArrayList!=null){
                                                for (Word w:wordArrayList
                                                ) {
                                                    if (counts[0] <num){

                                                        counts[0]+=1;
                                                        WordLearnSubWord wordLearnSubWord = new WordLearnSubWord();
                                                        Liju liju=   lijuService.getByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                        ArrayList<FrontLiJu> frontLiJus = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(liju.getSentences(),FrontLiJu.class) ;
                                                        ArrayList<Word> cizus =  wordService.getCizuByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                        Integer wordsCount = wordService.count(String.valueOf(w.getWord().charAt(0)).toLowerCase());
                                                        ArrayList<Word> choose = new ArrayList<>();
                                                        for (int i = 0; i <3 ; i++) {
                                                            Integer random =randomUtil.getRandomRange(0,wordsCount);
                                                            Word word = wordService.getRandom(String.valueOf(w.getWord().charAt(0)).toLowerCase(),random,1);
                                                            choose.add(word);

                                                        }
                                                        Word chooseSelf = new Word();
                                                        chooseSelf.setWord(w.getWord());
                                                        chooseSelf.setCreatetime(w.getCreatetime());
                                                        chooseSelf.setCharac(w.getCharac());
                                                        chooseSelf.setSoundmark1(w.getSoundmark1());
                                                        chooseSelf.setSoundmark2(w.getSoundmark2());
                                                        chooseSelf.setTrans(w.getTrans());
                                                        chooseSelf.setDeleted(w.getDeleted());
                                                        chooseSelf.setId(w.getId());
                                                        chooseSelf.setUpdatetime(w.getUpdatetime());
                                                        choose.add(chooseSelf);
                                                        Collections.shuffle(choose);


                                                        ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
                                                        wordLearnSubWord.setInBook(freshwordService.handleWordBoolean(w,exitsWordArrayList));

                                                        wordLearnSubWord.setType(key1);
                                                        wordLearnSubWord.setChoose(choose);
                                                        wordLearnSubWord.setWord(w);
                                                        wordLearnSubWord.setCikuexampleid(0);
                                                        wordLearnSubWord.setCizu(cizus);
                                                        wordLearnSubWord.setLiju(frontLiJus);
                                                        wordLearnSubWords.add(wordLearnSubWord);
                                                        index++;
                                                    }

                                                }
                                            }

                                        }

                                    });
                                }else{
//                            returnVO.setMessage("获取nowrecite失败");
//                            returnVO.setCode(500);

                                }
                            }


                        });
                        wordLearn.setType(1);
                        wordLearn.setWords(wordLearnSubWords);
                        wordLearn.setNum(wordLearnSubWords.size());
                        returnVO.setMessage("获取成功");
                        returnVO.setCode(200);
                        //                   returnVO.setData(wordLearn);
                        returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordLearn));
                        return  returnVO;
                    }else{
                        returnVO.setMessage("已经背完了");
                        returnVO.setCode(200);
                    }
                }

            }else{
                returnVO.setMessage("获取recite失败");
                returnVO.setCode(500);
                return  returnVO;
            }

            return  returnVO;
        }

    }


    @RequestMapping(value = "/getLearing",method = RequestMethod.POST)
    public ReturnVO getLearing(@RequestParam(value = "reciteid")Integer reciteId,@RequestParam(value = "num",defaultValue = "20")Integer num,@RequestParam(value = "uid")Integer uid,@RequestParam(value = "type",defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        if (reciteId==null||num==null){
            returnVO.setMessage("数据不能为空");
            returnVO.setCode(500);
            return returnVO;
        }else{
            WordLearn wordLearn = new WordLearn();

            Resite resite = resiteService.getById(reciteId);
            if (resite!=null){
                if (type==0){
                    //词库
                    String learn =base64Util.decodeToString(resite.getLearn()) ;
                    if (!("".equals(learn))){

                        String[] learnWordIds = learn.split(",");
                        Integer[] cikuexampleids = new Integer[learnWordIds.length] ;
                        for (int i = 0; i <num ; i++) {
                            cikuexampleids[i]=Integer.valueOf(learnWordIds[i]);
                        }
                        Integer nowresiteid = resite.getNowresiteid();
                        Nowresite nowresite =nowresiteService.getById(nowresiteid);
                        if (nowresite!=null){
                            final Integer[] counts = {0};
                            ArrayList<Cikuexample> cikuexamples =  cikuexampleService.listByIds(nowresite.getCikutypeid(),nowresite.getCikuid(),cikuexampleids);
                            HashMap<Character,ArrayList<Cikuexample>>  classfiedCikuexamples = listUtil.cikuexampleListClassifyByInitial(cikuexamples);
                            ArrayList< WordLearnSubWord> wordLearnSubWords = new ArrayList<>();
                            classfiedCikuexamples.keySet().forEach(key ->{
                                ArrayList<Cikuexample> cikuexampleArrayList = classfiedCikuexamples.get(key);
                                if (counts[0] <num&&cikuexampleArrayList.size()!=0){

                                    //导入单词 进入单词service
                                    ArrayList<Word> wordArrayList= wordService.selectByIdBatch(String.valueOf(key), cikuexampleArrayList);
                                    Integer index = 0;
                                    if (counts[0] <num&&wordArrayList!=null){
                                        for (Word w:wordArrayList
                                        ) {
                                            if (counts[0] <num){
                                                counts[0] +=1;
                                                WordLearnSubWord wordLearnSubWord = new WordLearnSubWord();
                                                Liju liju=   lijuService.getByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                ArrayList<FrontLiJu> frontLiJus = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(liju.getSentences(),FrontLiJu.class) ;
                                                ArrayList<Word> hunxiaoci = wordService.SelectLikeByWordNoEq(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                ArrayList<Word> cizus =  wordService.getCizuByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                cizus.addAll(hunxiaoci);
                                                Integer wordsCount = wordService.count(String.valueOf(w.getWord().charAt(0)).toLowerCase());
                                                ArrayList<Word> choose = new ArrayList<>();
                                                for (int i = 0; i <3 ; i++) {
                                                    Integer random =randomUtil.getRandomRange(0,wordsCount);
                                                    Word word = wordService.getRandom(String.valueOf(w.getWord().charAt(0)).toLowerCase(),random,1);
                                                    choose.add(word);
                                                }
                                                Word chooseSelf = new Word();
                                                chooseSelf.setWord(w.getWord());
                                                chooseSelf.setCreatetime(w.getCreatetime());
                                                chooseSelf.setCharac(w.getCharac());
                                                chooseSelf.setSoundmark1(w.getSoundmark1());
                                                chooseSelf.setSoundmark2(w.getSoundmark2());
                                                chooseSelf.setTrans(w.getTrans());
                                                chooseSelf.setDeleted(w.getDeleted());
                                                chooseSelf.setId(w.getId());
                                                chooseSelf.setUpdatetime(w.getUpdatetime());
                                                choose.add(chooseSelf);
                                                Collections.shuffle(choose);
                                                ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
                                                wordLearnSubWord.setInBook(freshwordService.handleWordBoolean(w,exitsWordArrayList));
                                                wordLearnSubWord.setChoose(choose);
                                                wordLearnSubWord.setWord(w);
                                                wordLearnSubWord.setType("learn");
                                                wordLearnSubWord.setCikuexampleid(cikuexampleArrayList.get(index).getId());
                                                wordLearnSubWord.setCizu(cizus);
                                                wordLearnSubWord.setLiju(frontLiJus);
                                                wordLearnSubWords.add(wordLearnSubWord);
                                                index++;
                                            }

                                        }
                                    }
                                    ;
                                }

                            });
                            wordLearn.setType(0);
                            wordLearn.setNum(wordLearnSubWords.size());
                            wordLearn.setWords(wordLearnSubWords);
                            returnVO.setMessage("获取成功");
                            returnVO.setCode(200);
                            returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordLearn));
//                    returnVO.setData(wordLearn);
                            return  returnVO;

                        }else{
                            returnVO.setMessage("获取nowrecite失败");
                            returnVO.setCode(500);
                            return  returnVO;
                        }
                    }else{
                        returnVO.setMessage("已经背完了");
                        returnVO.setCode(200);
                    }
                }else{
                    //生词本
                    String learn =base64Util.decodeToString(resite.getLearn()) ;
                    if (!("".equals(learn))){
                        ArrayList<WordSimpleEnd> wordSimpleEnds =jsonUtil.parseJsonStrToArrayList(learn,WordSimpleEnd.class);
                            HashMap<Character,ArrayList<WordSimpleEnd>>  classfiedWordSimpleEnd = listUtil.wordSimpleEndListClassifyByInitial(wordSimpleEnds);
                            ArrayList< WordLearnSubWord> wordLearnSubWords = new ArrayList<>();
                            ArrayList<WordFull> wordFulls = new ArrayList<>();
                        final Integer[] counts = {0};
                            classfiedWordSimpleEnd.keySet().forEach(key ->{
                                ArrayList<WordSimpleEnd> wordSimpleEndArrayList = classfiedWordSimpleEnd.get(key);
                                if (counts[0] <num&&wordSimpleEndArrayList.size()!=0){

                                    //导入单词 进入单词service
                                    ArrayList<Word> wordArrayList= wordService.selectByIdBatch2(String.valueOf(key), wordSimpleEndArrayList);
                                    Integer index = 0;
                                    if (wordArrayList!=null){
                                        for (Word w:wordArrayList
                                        ) {
                                            if (counts[0] <num){
                                                counts[0] +=1;
                                                WordLearnSubWord wordLearnSubWord = new WordLearnSubWord();
                                                Liju liju=   lijuService.getByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                ArrayList<FrontLiJu> frontLiJus = jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(liju.getSentences(),FrontLiJu.class) ;
                                                ArrayList<Word> hunxiaoci = wordService.SelectLikeByWordNoEq(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                ArrayList<Word> cizus =  wordService.getCizuByWord(String.valueOf(w.getWord().charAt(0)).toLowerCase(),w.getWord());
                                                cizus.addAll(hunxiaoci);
                                                Integer wordsCount = wordService.count(String.valueOf(w.getWord().charAt(0)).toLowerCase());
                                                ArrayList<Word> choose = new ArrayList<>();
                                                for (int i = 0; i <3 ; i++) {
                                                    Integer random =randomUtil.getRandomRange(0,wordsCount);
                                                    Word word = wordService.getRandom(String.valueOf(w.getWord().charAt(0)).toLowerCase(),random,1);
                                                    choose.add(word);
                                                }
                                                Word chooseSelf = new Word();
                                                chooseSelf.setWord(w.getWord());
                                                chooseSelf.setCreatetime(w.getCreatetime());
                                                chooseSelf.setCharac(w.getCharac());
                                                chooseSelf.setSoundmark1(w.getSoundmark1());
                                                chooseSelf.setSoundmark2(w.getSoundmark2());
                                                chooseSelf.setTrans(w.getTrans());
                                                chooseSelf.setDeleted(w.getDeleted());
                                                chooseSelf.setId(w.getId());
                                                chooseSelf.setUpdatetime(w.getUpdatetime());
                                                choose.add(chooseSelf);
                                                Collections.shuffle(choose);
                                                ArrayList<Word> exitsWordArrayList =freshwordService.getWords(uid);
                                                wordLearnSubWord.setInBook(freshwordService.handleWordBoolean(w,exitsWordArrayList));
                                                wordLearnSubWord.setChoose(choose);
                                                wordLearnSubWord.setWord(w);
                                                wordLearnSubWord.setType("learn");
                                                wordLearnSubWord.setCikuexampleid(0);
                                                wordLearnSubWord.setCizu(cizus);
                                                wordLearnSubWord.setLiju(frontLiJus);
                                                wordLearnSubWords.add(wordLearnSubWord);
                                                index++;

                                            }

                                        }
                                    }
                                    ;
                                }

                            });
                            wordLearn.setType(1);
                            wordLearn.setNum(wordLearnSubWords.size());
                            wordLearn.setWords(wordLearnSubWords);
                            returnVO.setMessage("获取成功");
                            returnVO.setCode(200);
                            returnVO.setData(jsonUtil.parseObjectToJsonStrThenToBase64(wordLearn));
//                    returnVO.setData(wordLearn);
                            return  returnVO;

                    }else{
                        returnVO.setMessage("已经背完了");
                        returnVO.setCode(200);
                    }

                }

            }else{
                returnVO.setMessage("获取recite失败");
                returnVO.setCode(500);
                return  returnVO;
            }

            return  returnVO;
        }

    }
}
