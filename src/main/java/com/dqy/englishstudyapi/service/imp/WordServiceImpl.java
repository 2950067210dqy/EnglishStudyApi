package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.mapper.WordMapper;
import com.dqy.englishstudyapi.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.RequestUtil;
import com.dqy.englishstudyapi.vo.RequestResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
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
    public int importWords(ArrayList<String> words, Character initial) {
        if (words.size()==0||initial==null){
            return -1;
        }
        //筛选已经存在的单词
        List<String> notStoreInDBWords = new ArrayList<>();
        for (String word:words
             ) {
          Word existWord =  wordMapper.selectByWord(word,initial);
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
}
