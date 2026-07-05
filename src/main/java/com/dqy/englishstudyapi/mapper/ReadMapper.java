package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.entity.frontEntity.ReadSimple.ReadSimple;
import com.dqy.englishstudyapi.tablebean.Read;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@Mapper
public interface ReadMapper extends BaseMapper<Read> {

    void createTable(@Param("suffix") Integer suffix, @Param("suffix2") Integer suffix2);

    List<ReadSimple> selectSimple(@Param("suffix")Integer rid,  @Param("suffix2")Integer rsid);
}
