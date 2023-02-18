package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dqy.englishstudyapi.entity.frontEntity.ImportWordsEntity;
import com.dqy.englishstudyapi.service.WordService;
import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.mapper.CikuMapper;
import com.dqy.englishstudyapi.service.CikuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqy.englishstudyapi.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
public class CikuServiceImpl extends ServiceImpl<CikuMapper, Ciku> implements CikuService {
    @Autowired
    ListUtil listUtil;
    @Autowired
    CikuMapper cikuMapper;
    @Autowired
    WordService wordService;
    @Override
    public boolean importWords(ImportWordsEntity param) {
        if (param==null||param.getCikuTypeId()<1||param.getWords().size()==0){
            return false;
        }
        ArrayList<String > words = param.getWords();
        Collections.sort(words);
        HashMap<Character, ArrayList<String>> wordsClassified =listUtil.stringListClassifyByInitial(words);
        if (wordsClassified==null){
            return false;
        }
        wordsClassified.keySet().forEach(key ->{
            ArrayList<String> partWords = wordsClassified.get(key);
            if (partWords.size()!=0){
                //导入单词 进入单词service
               int result= wordService.importWords(partWords, String.valueOf(key));
            }
        });
        return true;
    }

    @Override
    public ArrayList<Ciku> list(Integer tableId) {

        return cikuMapper.selectAll(tableId);
    }
    @Override
    public List<Ciku> listByUid(Integer cikutypeId, Integer uid) {
        return cikuMapper.selectAllByUid(cikutypeId,uid);
    }

    @Override
    public boolean shouCang(Ciku shoucang) {
        return SqlHelper.retBool(cikuMapper.shouCang(shoucang));
    }

    @Override
    public boolean shouCangCancel(Ciku shoucang) {
        return SqlHelper.retBool(cikuMapper.shouCangCancel(shoucang));
    }


    @Override
    public Ciku selectByDsc(Integer cikuTypeId, String dsc) {
        return cikuMapper.selectByDesc(cikuTypeId,dsc);
    }

    @Override
    public boolean save(Integer cikuTypeId, Ciku ciku) {
        cikuMapper.createTable(cikuTypeId);
        return SqlHelper.retBool(cikuMapper.insert(cikuTypeId,ciku));
    }

    @Override
    public Ciku selectById(Integer cikuTypeId, Integer id) {
        return cikuMapper.selectById(cikuTypeId,id);
    }


}
