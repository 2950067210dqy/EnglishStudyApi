package com.dqy.englishstudyapi.entity.frontEntity.WordLearn;

import com.dqy.englishstudyapi.entity.frontEntity.FrontLiJu;
import com.dqy.englishstudyapi.tablebean.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class WordLearnSubWord {
    Integer cikuexampleid;
    Word word;
    Boolean inBook;
    ArrayList<Word> cizu;
    ArrayList<FrontLiJu> liju;
    ArrayList<Word> choose;
    String type;
}
