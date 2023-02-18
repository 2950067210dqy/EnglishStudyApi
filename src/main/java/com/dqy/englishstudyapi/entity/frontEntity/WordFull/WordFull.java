package com.dqy.englishstudyapi.entity.frontEntity.WordFull;

import com.dqy.englishstudyapi.entity.endEntity.WordEnd;
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
public class WordFull {
    String initial;
    ArrayList<WordEnd> words;
    Integer count;
}
