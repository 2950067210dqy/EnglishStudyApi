package com.dqy.englishstudyapi.service;

import com.dqy.englishstudyapi.entity.frontEntity.ReadSimple.ReadSimple;
import com.dqy.englishstudyapi.tablebean.Read;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
public interface ReadService extends IService<Read> {
    void createTable(Integer suffix, Integer suffix2);

    List<ReadSimple> selectSimple(Integer rid, Integer rsid);
}
