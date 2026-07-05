package com.dqy.englishstudyapi.entity.frontEntity.ScanInfo;

import com.dqy.englishstudyapi.entity.frontEntity.WordFull.WordFull;
import com.dqy.englishstudyapi.tablebean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.spi.LocaleNameProvider;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Repository
public class ScanInfo {
    User shareUser;
    String time;
    Integer count;
    ArrayList<WordFull> wordFulls;
}
