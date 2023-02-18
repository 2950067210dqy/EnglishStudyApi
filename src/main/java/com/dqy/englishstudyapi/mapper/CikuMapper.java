package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.tablebean.Ciku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邓亲优
 * @since 2023-02-03
 */
@Mapper
public interface CikuMapper extends BaseMapper<Ciku> {

    ArrayList<Ciku> selectAll(@Param("tableId") Integer tableId);

    Ciku selectByDesc(@Param("tableId")Integer cikuTypeId,@Param("dsc") String dsc);

//    @Options(useGeneratedKeys = true, keyProperty = "ciku.id", keyColumn = "id")
    Integer insert(@Param("tableId")Integer cikuTypeId,@Param("ciku") Ciku ciku);

    void createTable(@Param("tableId")Integer cikuTypeId);

    Ciku selectById(@Param("tableId")Integer cikuTypeId,@Param("id") Integer id);

    List<Ciku> selectAllByUid(@Param("tableId")Integer cikutypeId,@Param("uid")  Integer uid);


    Integer shouCang(@Param("ciku")Ciku shoucang);

    Integer shouCangCancel(@Param("ciku")Ciku shoucang);
}
