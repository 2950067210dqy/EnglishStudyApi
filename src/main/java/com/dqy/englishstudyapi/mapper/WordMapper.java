package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.tablebean.Word;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@Mapper
public interface WordMapper extends BaseMapper<Word> {

    Word selectByWord(@Param("word") String word,@Param("suffix") Character initial);
}
