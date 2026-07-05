package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicStudyCircleSetting;

import com.dqy.englishstudyapi.tablebean.Studycircle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository

public class BasicStudyCircleSetting {
    Long allCount;
    Long todayCount;

    List<StudyCircleSimpleFull> lastStudyCircles;
}
