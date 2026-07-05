package com.dqy.englishstudyapi.entity.adminEntity.administer;

import com.dqy.englishstudyapi.tablebean.Score;
import com.dqy.englishstudyapi.tablebean.User;
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
public class FrontScores {
    User user;
    Score score;
}
