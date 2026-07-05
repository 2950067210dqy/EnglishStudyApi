package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.tablebean.Freshword;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqy.englishstudyapi.tablebean.Word;

import java.util.ArrayList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-16
 */
public interface FreshwordService extends IService<Freshword> {

    ArrayList<Word> getWords(Integer uid);

    ArrayList<WordEnd> handleWords(ArrayList<Word> datas, ArrayList<Word> userDatas);
    WordEnd handleWord(Word data, ArrayList<Word> userDatas);
    boolean handleWordBoolean(Word data, ArrayList<Word> userDatas) ;
}
