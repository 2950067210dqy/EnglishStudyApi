package com.dqy.englishstudyapi.entity.frontEntity.GameData;

import com.dqy.englishstudyapi.tablebean.Gamedatamax;
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
public class FrontGameData {
    Gamedatamax gamedatamax;
    User user;
}
