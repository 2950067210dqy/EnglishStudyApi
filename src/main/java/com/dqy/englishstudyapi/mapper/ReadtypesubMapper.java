package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.tablebean.Readtypesub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邓亲优
 * @since 2023-03-02
 */
@Mapper
public interface ReadtypesubMapper extends BaseMapper<Readtypesub> {

    void createTable(@Param("suffix") Integer suffix);

}
