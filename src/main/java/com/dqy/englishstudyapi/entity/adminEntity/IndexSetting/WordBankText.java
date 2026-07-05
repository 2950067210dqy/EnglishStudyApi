package com.dqy.englishstudyapi.entity.adminEntity.IndexSetting;

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
public class WordBankText {
    String value;
    String desc;
}
