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
public class ZbOrderCondition extends  BaseCondition{
    String zbcodeSearch;

    String zborderidSearch;

    List<Integer> statusSelect;
    List<Integer> userSelect;

}
