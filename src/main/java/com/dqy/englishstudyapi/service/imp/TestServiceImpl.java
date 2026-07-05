package com.dqy.englishstudyapi.service.imp;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dqy.englishstudyapi.tablebean.Test;
import com.dqy.englishstudyapi.mapper.TestMapper;
import com.dqy.englishstudyapi.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-28
 */
@Service
//开启事务回滚
@Transactional(rollbackFor = RuntimeException.class)
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
    @Autowired
    TestMapper testMapper;
    @Override
    public List<Test> list(Integer testtype, Integer current, Integer size) {
        boolean result =  createTableIfNotExist(testtype);

        return testMapper.list(testtype,(current-1)*size,size);
    }
    @Override
    public List<Test> listRandom(Integer testtype, Integer random, Integer size) {
        boolean result =  createTableIfNotExist(testtype);

        return testMapper.list(testtype,random,size);
    }

    @Override
    public Integer count(Integer testtype) {
        boolean result =  createTableIfNotExist(testtype);
        return testMapper.count(testtype);
    }

    @Override
    public Test getById(Integer testtype, Integer testid) {
        boolean result =  createTableIfNotExist(testtype);
        return testMapper.getById(testtype,testid);
    }


    public boolean createTableIfNotExist(Integer testtype){
        return SqlHelper.retBool( testMapper.createTableIfNotExist(testtype)) ;
    }
}
