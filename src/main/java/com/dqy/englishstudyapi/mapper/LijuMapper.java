package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.tablebean.Liju;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-11
 */
@Mapper
public interface LijuMapper extends BaseMapper<Liju> {

    Liju getByWord(@Param("suffix") String initial,@Param("word") String word);

    void createTable(@Param("suffix")String initial);
}
