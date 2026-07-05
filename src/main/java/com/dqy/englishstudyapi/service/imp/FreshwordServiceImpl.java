package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFull;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Freshword;
import com.dqy.englishstudyapi.mapper.FreshwordMapper;
import com.dqy.englishstudyapi.service.FreshwordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.JsonUtil;
import com.dqy.englishstudyapi.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-16
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class FreshwordServiceImpl extends ServiceImpl<FreshwordMapper, Freshword> implements FreshwordService {
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    ListUtil listUtil;
    @Autowired
    WordService wordService;
    @Override
    public ArrayList<Word> getWords(Integer uid) {
        Freshword freshword =getOne(new QueryWrapper<Freshword>().eq("uid",uid));
        if (freshword!=null){
            ArrayList<WordSimpleEnd> words =jsonUtil.parseBase64ToJsonStrThenToJavaArrayList(freshword.getWords(),WordSimpleEnd.class);
            if (words!=null&&words.size()!=0){
                HashMap<Character,ArrayList<WordSimpleEnd>> classfiedWordSimpleEnd = listUtil.wordSimpleEndListClassifyByInitial(words);
                ArrayList<Word> wordss = new ArrayList<>();
                classfiedWordSimpleEnd.keySet().forEach(key ->{
                    ArrayList<WordSimpleEnd> wordSimpleEndS = classfiedWordSimpleEnd.get(key);
                    if (wordSimpleEndS.size()!=0){
                        //导入单词 进入单词service
                        ArrayList<Word> wordArrayList= wordService.selectByIdBatch2(String.valueOf(key), wordSimpleEndS);
                        wordss.addAll(wordArrayList);
                    }



                    });

                return wordss;


            }else{

                return  null;
            }
        }else{
            return  null;
        }

    }

    @Override
    public ArrayList<WordEnd> handleWords(ArrayList<Word> datas, ArrayList<Word> userDatas) {
        ArrayList<WordEnd> datass = new ArrayList<>();
        for (int i = 0; i < datas.size() ; i++) {
            WordEnd wordEnd = new WordEnd();
            wordEnd.setWord(datas.get(i));

            if ( userDatas!=null){
                boolean flag =true;
                for (int j = 0; j < userDatas.size(); j++) {
                    if (datas.get(i).getWord().equals(userDatas.get(j).getWord())){
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    wordEnd.setInBook(false);
                }else{
                    wordEnd.setInBook(true);
                }
            }else{
                wordEnd.setInBook(false);
            }


            datass.add(wordEnd);
        }
        return datass;
    }

    @Override
    public WordEnd handleWord(Word data, ArrayList<Word> userDatas) {

            WordEnd wordEnd = new WordEnd();
            wordEnd.setWord(data);
            if ( userDatas!=null){
            boolean flag =true;
            for (int j = 0; j < userDatas.size(); j++) {
                if (data.getWord().equals(userDatas.get(j).getWord())){
                    flag=false;
                    break;
                }
            }
            if (flag){
                wordEnd.setInBook(false);
            }else{
                wordEnd.setInBook(true);
            }
        }else{
            wordEnd.setInBook(false);
        }


        return wordEnd;
    }

    public boolean handleWordBoolean(Word data, ArrayList<Word> userDatas) {

        if ( userDatas!=null){
            boolean flag =true;
            for (int j = 0; j < userDatas.size(); j++) {
                if (data.getWord().equals(userDatas.get(j).getWord())){
                    flag=false;
                    break;
                }
            }
            if (flag){
                return false;
            }else{
                 return true;
            }
        }else{
            return false;
        }



    }
}
