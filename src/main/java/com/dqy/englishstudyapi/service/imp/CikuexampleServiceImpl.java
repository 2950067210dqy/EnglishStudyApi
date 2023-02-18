package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.mapper.CikuexampleMapper;
import com.dqy.englishstudyapi.service.CikuexampleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@Service
public class CikuexampleServiceImpl extends ServiceImpl<CikuexampleMapper, Cikuexample> implements CikuexampleService {
    @Autowired
    CikuexampleMapper cikuexampleMapper;
    @Override
    public Integer count(Integer cikutypeId, Integer cikuId) {
        return cikuexampleMapper.count(cikutypeId,cikuId);
    }

    @Override
    public boolean insertBatch(Integer cikuTypeId, Integer cikuId, ArrayList<Cikuexample> cikuexamples) {
        cikuexampleMapper.createTable(cikuTypeId, cikuId);
        return SqlHelper.retBool(cikuexampleMapper.insertBatch(cikuTypeId,cikuId,cikuexamples));
    }

    @Override
    public ArrayList<Cikuexample> list(Integer cikuTypeId, Integer cikuId) {
        return cikuexampleMapper.select(cikuTypeId,cikuId);
    }

    @Override
    public ArrayList<Cikuexample> listByIds(Integer cikutypeid, Integer cikuid, Integer[] cikuexampleids) {
        return cikuexampleMapper.selectByIds(cikutypeid,cikuid,cikuexampleids);
    }
}
