package com.dqy.englishstudyapi.entity.frontEntity;

import com.dqy.englishstudyapi.tablebean.Scoresource;
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
public class FrontScoreSource {
    String dsc;
    Integer type;
    Scoresource scoresource;
}
