package com.dqy.englishstudyapi.mapper;

import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import com.dqy.englishstudyapi.tablebean.Word;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;

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

    Word selectByWord(@Param("suffix") String initial,@Param("word") String word);

    ArrayList<Word> selectByWordBatch(@Param("suffix") String initial,@Param("words") ArrayList<String> partWords);

    ArrayList<Word> selectByIdBatch(@Param("suffix")String initial,@Param("exp") ArrayList<Cikuexample> cikuexampleArrayList);

    ArrayList<Word> selectByWordLike(@Param("suffix")String initial,@Param("word") String word);

    Integer count(@Param("suffix")String initial);

    Word getRandom(@Param("suffix")String initial,@Param("random")Integer random,@Param("limit") Integer limit);

    ArrayList<Word> selectByIdBatch2(@Param("suffix")String initial,@Param("wse") ArrayList<WordSimpleEnd> wordSimpleEndArrayList);

    ArrayList<Word> SelectLikeRightByWord(@Param("suffix")String initial, @Param("word") String word);

    ArrayList<Word> SelectLikeByWordNoEq(@Param("suffix")String initial,@Param("word") String word);


    ArrayList<Word> SelectLikeLeftByWord(@Param("suffix")String initial, @Param("word")String word);

    ArrayList<Word> SelectLikeByWord(@Param("suffix")String initial,@Param("word") String word);

    ArrayList<Word> selectByTrans(@Param("suffix")String initial,@Param("trans") String word);

    void createTable(@Param("suffix")String initial);
}
