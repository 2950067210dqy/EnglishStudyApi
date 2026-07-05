package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicStudyCircleSetting;

import com.dqy.englishstudyapi.tablebean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository

public class StudyCircleSimpleFull {
    User user;
    String title;
    LocalDateTime updatetime;
}
