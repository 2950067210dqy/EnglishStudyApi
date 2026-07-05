package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.tablebean.Readtypesub;
import com.dqy.englishstudyapi.mapper.ReadtypesubMapper;
import com.dqy.englishstudyapi.service.ReadtypesubService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@Service
public class ReadtypesubServiceImpl extends ServiceImpl<ReadtypesubMapper, Readtypesub> implements ReadtypesubService {
    @Autowired
    ReadtypesubMapper readtypesubMapper;

     public void createTable(Integer suffix){
         readtypesubMapper.createTable(suffix);
    }

}
