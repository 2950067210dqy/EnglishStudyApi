package com.dqy.englishstudyapi.entity.Pay;

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
public class PaySession {
    Integer uid;
    Integer dsc;
    Integer price;
    String orderNum;
}
