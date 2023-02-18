package com.dqy.englishstudyapi.entity.frontEntity.WordFull;

import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Freshword;
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
public class WordFullParent {
    Ciku ciku;
    Freshword freshword;
    ArrayList<WordFull> wordFulls;
    Integer count;
}
