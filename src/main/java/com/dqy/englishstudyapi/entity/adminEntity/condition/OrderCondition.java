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
public class OrderCondition extends BaseCondition{
    @JsonProperty(value = "orderstatus")
    Integer orderstatus;
    String ordersSearch;
    List<Integer> productSelect;

    List<Integer> userSelect;
}
