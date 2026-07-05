package com.dqy.englishstudyapi.entity.adminEntity.administer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class WordTree {
    String text;
    String value;
    boolean disable;
    List<WordTree> children;
}
