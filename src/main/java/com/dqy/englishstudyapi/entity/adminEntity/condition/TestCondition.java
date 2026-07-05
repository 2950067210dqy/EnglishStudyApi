package com.dqy.englishstudyapi.entity.adminEntity.condition;

import io.swagger.models.auth.In;
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
public class TestCondition extends  BaseCondition{
    Integer testType;

    String titleSearch;
    String optaSearch;
    String optbSearch;
    String optcSearch;
    String optdSearch;

    String analySearch;
    List<Integer> answerSelect;

}
