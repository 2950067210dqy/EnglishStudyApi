package com.dqy.englishstudyapi.entity.adminEntity.condition;

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
public class CikuCondition extends BaseCondition {

    String dscSearch;
    String dscabbSearch;
    List<Integer> userSelect;
    Integer cikuTypeId;

}
