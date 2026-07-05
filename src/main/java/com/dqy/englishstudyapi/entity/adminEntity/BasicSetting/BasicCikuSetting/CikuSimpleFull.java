package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicCikuSetting;

import com.dqy.englishstudyapi.tablebean.Ciku;
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
public class CikuSimpleFull {
    Ciku ciku;
    Integer count;
    String parentDsc;
}
