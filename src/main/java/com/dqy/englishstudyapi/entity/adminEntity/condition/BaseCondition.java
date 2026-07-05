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
public class BaseCondition {
    List<String> orderbyDesc;
    List<String> orderbyAsc;
    Integer current;
    Integer size;
    Integer sizeBackUp;

    Integer total;
}
