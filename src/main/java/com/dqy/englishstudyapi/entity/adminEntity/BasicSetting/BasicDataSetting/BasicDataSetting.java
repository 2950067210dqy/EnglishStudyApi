package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicDataSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.function.LongToDoubleFunction;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class BasicDataSetting {
    Long todayStudyCount;
    Long allStudyCount;
    Long todayReviewCount;
    Long allReviewCount;
    Long todayTimeCount;
    Long allTimeCount;
    Long todayUserCount;
    Long allUserCount;
}
