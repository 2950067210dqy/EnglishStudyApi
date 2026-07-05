package com.dqy.englishstudyapi.entity.adminEntity.administer;

import com.dqy.englishstudyapi.tablebean.Ciku;
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
public class FrontCikus {
    User user;
    Ciku ciku;
    Integer wordCount;
}
