package com.dqy.englishstudyapi.entity.adminEntity.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductCondition extends BaseCondition{

    @JsonProperty(value = "pType")
    Integer pType;
    @JsonProperty(value = "pTypeSub")
    Integer pTypeSub;
    String titleSearch;

    List<Integer> merchantSelect;
}
