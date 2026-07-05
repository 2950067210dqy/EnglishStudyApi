package com.dqy.englishstudyapi.service.imp;

import com.dqy.englishstudyapi.entity.frontEntity.ReadSimple.ReadSimple;
import com.dqy.englishstudyapi.tablebean.Read;
import com.dqy.englishstudyapi.mapper.ReadMapper;
import com.dqy.englishstudyapi.service.ReadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@Service
public class ReadServiceImpl extends ServiceImpl<ReadMapper, Read> implements ReadService {
    @Autowired
    ReadMapper readMapper;
    @Override
    public void createTable(Integer suffix, Integer suffix2) {
        readMapper.createTable(suffix,suffix2);
    }

    @Override
    public List<ReadSimple> selectSimple(Integer rid, Integer rsid) {
        return readMapper.selectSimple(rid,rsid);
    }
}
