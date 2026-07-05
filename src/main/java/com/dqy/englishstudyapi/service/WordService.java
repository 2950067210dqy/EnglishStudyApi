package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Word;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
public interface WordService extends IService<Word> {

    int importWords(ArrayList<String> words, String initial);

    Word selectByWord(String initial, String word);

    ArrayList<Word> selectByWordBatch(String initial, ArrayList<String> partWords);

    ArrayList<Word> selectByIdBatch(String initial, ArrayList<Cikuexample> cikuexampleArrayList);
    ArrayList<Word> selectByIdBatch2(String initial, ArrayList<WordSimpleEnd> WordSimpleEndArrayList);
    ArrayList<Word> getCizuByWord(String initial, String word);

    Integer count(String initial);

    Word getRandom(String initial,Integer random, Integer limit);

    ArrayList<Word> SelectLikeRightByWord(String initial, String word);

    ArrayList<Word> SelectLikeByWordNoEq(String initial, String word);


    ArrayList<Word> SelectLikeLeftByWord(String initial, String word);

    ArrayList<Word> SelectLikeByWord(String initial, String word);

    ArrayList<Word> selectAllByTrans(String word);

    void createTable(String initial);
}
