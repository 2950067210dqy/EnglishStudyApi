package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFull;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.mapper.WordMapper;
import com.dqy.englishstudyapi.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {
    @Autowired
    WordMapper wordMapper;
    @Autowired
    RequestUtil requestUtil;
    @Autowired
    ListUtil listUtil;
    @Override
    public int importWords(ArrayList<String> words, String initial) {
        if (words.size()==0||initial==null){
            return -1;
        }
        //筛选已经存在的单词
        List<String> notStoreInDBWords = new ArrayList<>();
        for (String word:words
             ) {
          Word existWord =  wordMapper.selectByWord(initial,word);
          if (existWord==null){
              notStoreInDBWords.add(word);
          }
        }
        if (notStoreInDBWords.size()==0){
            return 1;
        }else{
            //找数据
            for (String notStoreInDBWord:notStoreInDBWords
                 ) {


            }
            return 1;
        }


    }

    @Override
    public Word selectByWord(String initial, String word) {
        createTable(initial);
        return wordMapper.selectByWord(initial,word);
    }

    @Override
    public ArrayList<Word> selectByWordBatch(String initial, ArrayList<String> partWords) {
        createTable(initial);
        return wordMapper.selectByWordBatch(initial,partWords);
    }

    @Override
    public ArrayList<Word> selectByIdBatch(String initial, ArrayList<Cikuexample> cikuexampleArrayList) {
        createTable(initial);
        return wordMapper.selectByIdBatch(initial,cikuexampleArrayList);
    }

    @Override
    public ArrayList<Word> selectByIdBatch2(String initial, ArrayList<WordSimpleEnd> WordSimpleEndArrayList) {
        createTable(initial);
        return wordMapper.selectByIdBatch2(initial,WordSimpleEndArrayList);
    }


    @Override
    public ArrayList<Word> getCizuByWord(String initial, String word) {
        createTable(initial);
        return wordMapper.selectByWordLike(initial,word);
    }

    @Override
    public Integer count(String initial) {

        createTable(initial);
        return wordMapper.count(initial);
    }

    @Override
    public Word getRandom(String initial,Integer random, Integer limit) {
        createTable(initial);
        return wordMapper.getRandom(initial,random,limit);
    }

    @Override
    public ArrayList<Word> SelectLikeRightByWord(String initial, String word) {
        createTable(initial);
        return wordMapper.SelectLikeRightByWord(initial, word);
    }
    @Override
    public ArrayList<Word> SelectLikeLeftByWord(String initial, String word) {
        createTable(initial);
        return wordMapper.SelectLikeLeftByWord(initial, word);
    }

    @Override
    public ArrayList<Word> SelectLikeByWord(String initial, String word) {
        createTable(initial);
        return wordMapper.SelectLikeByWord(initial, word);
    }

    @Override
    public ArrayList<Word> selectAllByTrans(String word) {
        HashMap<Character,ArrayList<Word>> wordmaps =listUtil.getEmptyWordListClassifyByInitial();
        ArrayList<Word> fulls = new ArrayList<>();
        wordmaps.keySet().forEach(key -> {
            ArrayList<Word> words = wordmaps.get(key);
            createTable(String.valueOf(key));
            words=wordMapper.selectByTrans(String.valueOf(key),word);
            if (words!=null&&words.size()!=0){
                fulls.addAll(words);
            }
        });

        return fulls;
    }

    @Override
    public void createTable(String initial) {
        wordMapper.createTable(initial);
    }

    @Override
    public ArrayList<Word> SelectLikeByWordNoEq(String initial, String word) {
        createTable(initial);
        return wordMapper.SelectLikeByWordNoEq(initial, word);
    }




}
