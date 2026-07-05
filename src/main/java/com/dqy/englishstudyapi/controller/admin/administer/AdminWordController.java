package com.dqy.englishstudyapi.controller.admin.administer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqy.englishstudyapi.entity.adminEntity.administer.*;
import com.dqy.englishstudyapi.entity.adminEntity.condition.CikuCondition;
import com.dqy.englishstudyapi.entity.adminEntity.condition.WordCondition;
import com.dqy.englishstudyapi.entity.page.MyPage;
import com.dqy.englishstudyapi.service.*;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Liju;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.*;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("adminWord")
public class AdminWordController {
    @Autowired
    WordService wordService;
    @Autowired
    UserService userService;
    @Autowired
    LijuService lijuService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    TimeUtil timeUtil;
    @Autowired
    DynamicTableNameUtil dynamicTableNameUtil;
    @Autowired
    ListUtil listUtil;
    @Autowired
    WordUtil wordUtil;
    ReturnVO returnVO;

    @PostMapping("/deleteWordSingle")
    public ReturnVO deleteWordSingle(@RequestParam("initial") String initial,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (initial==null||initial.equals("")||id==null){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        wordService.createTable(initial);
        dynamicTableNameUtil.SetTableName("word","_"+initial);
        boolean result = wordService.removeById(id);
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
    @PostMapping("/deleteWordBatch")
    public ReturnVO deleteWordBatch(@RequestParam("ids") List<Integer> ids,@RequestParam("initial") String initial){
        returnVO = new ReturnVO();
        if (initial==null||initial.equals("")||ids==null||ids.size()==0){
            returnVO.setMessage("参数为空");
            returnVO.setCode(500);
            return  returnVO;
        }
        wordService.createTable(initial);
        dynamicTableNameUtil.SetTableName("word","_"+initial);
        boolean result = wordService.removeBatchByIds(ids);
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
    public ReturnVO insert(@RequestBody FrontWords word){
        returnVO =  new ReturnVO();
        if (word==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Word newWord   =word.getWord();
        newWord.setDeleted(0);
        newWord.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
        newWord.setUpdatetime(timeUtil.getNowLocalDateTime());
        wordService.createTable(newWord.getWord().substring(0,1).toLowerCase().toString());
        dynamicTableNameUtil.SetTableName("word","_"+newWord.getWord().substring(0,1).toLowerCase().toString());
        boolean result =  wordService.save(newWord);
        if (result){
            Liju liju = word.getLiju();
            liju.setWord(newWord.getWord());
            liju.setDeleted(0);
            liju.setUpdatetime(timeUtil.getNowLocalDateTime());
            liju.setCreatetime(timeUtil.getNowLocalDateTime());
            lijuService.createTable(newWord.getWord().substring(0,1).toLowerCase().toString());
            dynamicTableNameUtil.SetTableName("liju","_"+newWord.getWord().substring(0,1).toLowerCase().toString());
            result = lijuService.save(liju);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("保存成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("保存例句失败");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("保存单词失败");
            return returnVO;
        }
    }
    @PostMapping("/update")
    public ReturnVO update(@RequestBody FrontWords word){
        returnVO =  new ReturnVO();
        if (word==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Word newWord = word.getWord();
        newWord.setUpdatetime(timeUtil.getNowLocalDateTime());
        wordService.createTable(newWord.getWord().substring(0,1).toLowerCase().toString());
        dynamicTableNameUtil.SetTableName("word","_"+newWord.getWord().substring(0,1).toLowerCase().toString());
        boolean result =  wordService.updateById(newWord);
        if (result){
            Liju liju = word.getLiju();
            liju.setUpdatetime(timeUtil.getNowLocalDateTime());
            lijuService.createTable(newWord.getWord().substring(0,1).toLowerCase().toString());
            dynamicTableNameUtil.SetTableName("liju","_"+newWord.getWord().substring(0,1).toLowerCase().toString());
            result = lijuService.updateById(liju);
            if (result){
                returnVO.setCode(200);
                returnVO.setMessage("更新成功");
                return returnVO;
            }else{
                returnVO.setCode(500);
                returnVO.setMessage("更新例句失败");
                return returnVO;
            }
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("更新单词失败");
            return returnVO;
        }
    }


    @PostMapping("/choosePickAll")
    public ReturnVO choosePickAll(){
        returnVO  = new ReturnVO();
        List<WordTree> wordTrees = new ArrayList<>();
        for (Character initial:listUtil.abs
             ) {
            WordTree wordTree = new WordTree();
            wordTree.setText(initial.toString());
            wordTree.setValue(initial.toString());
            wordTree.setDisable(false);

            List<WordTree> subWordTrees = new ArrayList<>();
            wordService.createTable(initial.toString());
            dynamicTableNameUtil.SetTableName("word","_"+initial.toString());
            List<Word> words = wordService.list();
            for (Word word:words
                 ) {
                WordTree subWordTree = new WordTree();
                subWordTree.setDisable(false);
                subWordTree.setText(word.getWord());
                subWordTree.setValue(word.getWord());
                subWordTrees.add(subWordTree);
            }
            wordTree.setChildren(subWordTrees);
            wordTrees.add(wordTree);
        }
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData(jsonUtil.parseArrayListToJsonStrThenToBase64((ArrayList) wordTrees));
        return  returnVO;
    }

    @PostMapping("/choosePick")
    public ReturnVO choosePick(){
        returnVO  = new ReturnVO();
        List<WordTree> wordTrees = new ArrayList<>();
        for (Character initial:listUtil.abs
        ) {
            WordTree wordTree = new WordTree();
            wordTree.setText(initial.toString());
            wordTree.setValue(initial.toString());
            wordTree.setDisable(false);

            wordTrees.add(wordTree);
        }
        returnVO.setCode(200);
        returnVO.setMessage("获取成功");
        returnVO.setData( wordTrees);
        return  returnVO;
    }
    @PostMapping("/getInitial")
    public ReturnVO getInitial(@RequestParam("initial")String initial){
        returnVO = new ReturnVO();
        if (initial==null||initial.equals("")){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return  returnVO;
        }
        wordService.createTable(initial.toString());
        dynamicTableNameUtil.SetTableName("word","_"+initial.toString());
        List<Word> words = wordService.list();
        if (words!=null&&words.size()!=0){
            List<WordFrontSimple> wordSimples = new ArrayList<>();
            for (Word word:words
                 ) {
                WordFrontSimple wordFrontSimple = new WordFrontSimple();
                wordFrontSimple.setWord(word.getWord());
                wordFrontSimple.setTrans(word.getTrans());
                wordFrontSimple.setId(word.getId());
                wordSimples.add(wordFrontSimple);
            }
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(jsonUtil.parseArrayListToJsonStrThenToBase64((ArrayList) wordSimples));
            return  returnVO;
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败或为空");
            return  returnVO;
        }

    }
    @PostMapping("/getAllWordsByInitial")
    public ReturnVO getAllWordsByInitial(@RequestBody WordCondition condition){
        returnVO = new ReturnVO();
        if (condition==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        Page<Word> page = new Page<>();
        page.setCurrent(condition.getCurrent());
        page.setSize(condition.getSize());
        wordService.createTable(condition.getInitial().toString());
        dynamicTableNameUtil.SetTableName("word","_"+condition.getInitial().toString());
        IPage<Word> iPage = wordService.page(page,getConditionWrapper(new QueryWrapper<Word>(),condition));
        if (iPage.getRecords()!=null){
            List<Word> words =iPage.getRecords().size()==0?new ArrayList<>(): (ArrayList<Word>) iPage.getRecords();
            ArrayList<FrontWords> frontWords = new ArrayList<>();
            for (Word w:words
            ) {
                FrontWords frontWord = new FrontWords();
                frontWord.setWord(w);
                Liju liju = lijuService.getByWord(condition.getInitial(), w.getWord());
                frontWord.setLiju(liju);
                frontWords.add(frontWord);
            }
            MyPage<FrontWords> myPage = new MyPage<>();
            myPage.setData(frontWords);
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


    @PostMapping("/getOneByInitialAndId")
    public ReturnVO getOneByInitialAndId(@RequestParam("initial")String initial,@RequestParam("id")Integer id){
        returnVO = new ReturnVO();
        if (initial==null||initial.equals("")||id==null){
            returnVO.setCode(500);
            returnVO.setMessage("数据为空");
            return returnVO;
        }

        wordService.createTable(initial);
        dynamicTableNameUtil.SetTableName("word","_"+initial);
        Word word = wordService.getOne(new QueryWrapper<Word>().eq("id",id));
        if (word!=null){
            FrontWords frontWord = new FrontWords();
            frontWord.setWord(word);
            Liju liju = lijuService.getByWord(initial, word.getWord());
            frontWord.setLiju(liju);
            returnVO.setCode(200);
            returnVO.setMessage("获取成功");
            returnVO.setData(frontWord);
        }else{
            returnVO.setCode(500);
            returnVO.setMessage("获取失败");
        }

        return returnVO;

    }

    @RequestMapping(value = "/getOne",method = RequestMethod.POST)
    public ReturnVO getOneOpen(@RequestParam("word") String word ,@RequestParam(value = "type",required = false,defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        if (wordUtil.isContainChinese(word)) {
            word = wordUtil.replaceEnglish(word);
            word = wordUtil.replaceNumber(word);
            word = wordUtil.filterNotation(word);
            word=wordUtil.filter(word);

            ArrayList<Word> words =wordService.selectAllByTrans(word);
            if (words!=null&&words.size()!=0){
                List<WordFrontSimple> wordSimples = new ArrayList<>();
                for (Word w:words
                ) {
                    WordFrontSimple wordFrontSimple = new WordFrontSimple();
                    wordFrontSimple.setWord(w.getWord());
                    wordFrontSimple.setTrans(w.getTrans());
                    wordFrontSimple.setId(w.getId());
                    wordSimples.add(wordFrontSimple);
                }
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(jsonUtil.parseArrayListToJsonStrThenToBase64((ArrayList) wordSimples));
                return  returnVO;
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
            if (word.length()==0){
                returnVO.setCode(500);
                returnVO.setMessage("单词错误");
                return  returnVO;
            }
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
                List<WordFrontSimple> wordSimples = new ArrayList<>();
                for (Word w:words
                ) {
                    WordFrontSimple wordFrontSimple = new WordFrontSimple();
                    wordFrontSimple.setWord(w.getWord());
                    wordFrontSimple.setTrans(w.getTrans());
                    wordFrontSimple.setId(w.getId());
                    wordSimples.add(wordFrontSimple);
                }
                returnVO.setCode(200);
                returnVO.setMessage("获取成功");
                returnVO.setData(jsonUtil.parseArrayListToJsonStrThenToBase64((ArrayList) wordSimples));
                return  returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("查不到这个单词");
                return  returnVO;
            }
        }


    }
    @RequestMapping(value = "/getExist",method = RequestMethod.POST)
    public ReturnVO getExist(@RequestParam("word") String word ,@RequestParam(value = "type",required = false,defaultValue = "0")Integer type){
        returnVO = new ReturnVO();
        if (wordUtil.isContainChinese(word)) {
                returnVO.setCode(200);
                returnVO.setMessage("单词错误");
                return  returnVO;

        }else{
            word = wordUtil.replaceNumber(word);
            word = wordUtil.replaceChinese(word);
            word = wordUtil.filterNotation(word);
            word=wordUtil.filter(word);
            if (word.length()==0){
                returnVO.setCode(200);
                returnVO.setMessage("单词错误");
                return  returnVO;
            }
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
                returnVO.setMessage("存在该单词");

                return  returnVO;

            }else{
                returnVO.setCode(500);
                returnVO.setMessage("查不到这个单词");
                return  returnVO;
            }
        }


    }
    private QueryWrapper<Word> getConditionWrapper(QueryWrapper<Word> wrapper, WordCondition condition) {
        if (wrapper==null){
            wrapper = new QueryWrapper<>();
        }
        if (condition.getWordSearch()!=null){
            wrapper = wrapper.like("word",condition.getWordSearch());
        }
        if (condition.getTransSearch()!=null){
            wrapper = wrapper.like("trans",condition.getTransSearch());
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
