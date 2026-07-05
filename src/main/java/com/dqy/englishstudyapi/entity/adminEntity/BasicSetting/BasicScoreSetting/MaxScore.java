package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicScoreSetting;

import com.dqy.englishstudyapi.tablebean.User;
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
public class MaxScore {
    User user;
    Long num;
}
