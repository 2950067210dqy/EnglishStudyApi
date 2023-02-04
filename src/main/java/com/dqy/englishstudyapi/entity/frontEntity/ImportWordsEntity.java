package com.dqy.englishstudyapi.entity.frontEntity;

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
public class ImportWordsEntity {
    ArrayList<String> words;
    Integer cikuTypeId;
    Integer cikuId;
}
