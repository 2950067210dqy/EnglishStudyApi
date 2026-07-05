package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Liju;
import com.dqy.englishstudyapi.mapper.LijuMapper;
import com.dqy.englishstudyapi.service.LijuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-11
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class LijuServiceImpl extends ServiceImpl<LijuMapper, Liju> implements LijuService {
    @Autowired
    LijuMapper lijuMapper;
    @Override
    public Liju getByWord(String initial, String word) {
        createTable(initial);
        return lijuMapper.getByWord(initial,word);
    }

    @Override
    public void createTable(String initial) {
        lijuMapper.createTable(initial);
    }
}
