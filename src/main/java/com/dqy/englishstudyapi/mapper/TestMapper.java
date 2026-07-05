package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.tablebean.Test;
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
 * @since 2023-02-28
 */
@Mapper
public interface TestMapper extends BaseMapper<Test> {

    Integer createTableIfNotExist(@Param("suffix") Integer testtype);

    List<Test> list(@Param("suffix")Integer testtype,@Param("current") Integer current,@Param("size") Integer size);

    Integer count(@Param("suffix")Integer testtype);

    Test getById(@Param("suffix")Integer testtype,@Param("id") Integer testid);
}
