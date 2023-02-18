package com.dqy.englishstudyapi.entity.endEntity;

import com.dqy.englishstudyapi.tablebean.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class WordEnd {
    Word word;
    boolean isInBook;
}
