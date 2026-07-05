package com.dqy.englishstudyapi.entity.frontEntity;

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
public class NowReciteSimple {
    Integer nowreciteid;
    Integer uid;
    Integer cikuTypeId;
    Integer cikuId;
    String dscabb;
    String dsc;
    Integer type;
    Integer reciteid;
    Integer count;
    Integer learnCount;
    Integer reviewCount;
}
