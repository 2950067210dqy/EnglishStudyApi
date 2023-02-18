package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@Mapper
public interface CikuexampleMapper extends BaseMapper<Cikuexample> {

    Integer count(@Param("cikuId1") Integer cikutypeId, @Param("cikuId2") Integer cikuId);

    Integer insertBatch(@Param("cikuId1")Integer cikuTypeId, @Param("cikuId2")Integer cikuId,@Param("cikuexamples") ArrayList<Cikuexample> cikuexamples);

    void createTable(@Param("cikuId1")Integer cikuTypeId, @Param("cikuId2")Integer cikuId);

    ArrayList<Cikuexample> select(@Param("cikuId1")Integer cikuTypeId,@Param("cikuId2") Integer cikuId);

    ArrayList<Cikuexample> selectByIds(@Param("cikuId1")Integer cikutypeid,@Param("cikuId2") Integer cikuid,@Param("ids") Integer[] cikuexampleids);
}
