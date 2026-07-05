package com.dqy.englishstudyapi.entity.adminEntity.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class ReadCondition extends BaseCondition{
    Integer readType;
    Integer readTypeSub;
    String essaySearch;
    String nameSearch;
    String authorSearch;
    String briefSearch;
}
