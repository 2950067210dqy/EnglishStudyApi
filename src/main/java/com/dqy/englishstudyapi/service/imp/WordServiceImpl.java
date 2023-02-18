package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.mapper.WordMapper;
import com.dqy.englishstudyapi.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {
    @Autowired
    WordMapper wordMapper;
    @Autowired
    RequestUtil requestUtil;
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
        return wordMapper.selectByWord(initial,word);
    }

    @Override
    public ArrayList<Word> selectByWordBatch(String initial, ArrayList<String> partWords) {
        return wordMapper.selectByWordBatch(initial,partWords);
    }

    @Override
    public ArrayList<Word> selectByIdBatch(String initial, ArrayList<Cikuexample> cikuexampleArrayList) {
        return wordMapper.selectByIdBatch(initial,cikuexampleArrayList);
    }

    @Override
    public ArrayList<Word> selectByIdBatch2(String initial, ArrayList<WordSimpleEnd> WordSimpleEndArrayList) {
        return wordMapper.selectByIdBatch2(initial,WordSimpleEndArrayList);
    }


    @Override
    public ArrayList<Word> getCizuByWord(String initial, String word) {
        return wordMapper.selectByWordLike(initial,word);
    }

    @Override
    public Integer count(String initial) {
        return wordMapper.count(initial);
    }

    @Override
    public Word getRandom(String initial,Integer random, Integer limit) {
        return wordMapper.getRandom(initial,random,limit);
    }

    @Override
    public ArrayList<Word> SelectLikeRightByWord(String initial, String word) {
        return wordMapper.SelectLikeRightByWord(initial, word);
    }

    @Override
    public ArrayList<Word> SelectLikeByWordNoEq(String initial, String word) {
        return wordMapper.SelectLikeByWordNoEq(initial, word);
    }


}
