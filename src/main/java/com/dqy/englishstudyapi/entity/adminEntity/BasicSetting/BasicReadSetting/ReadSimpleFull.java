package com.dqy.englishstudyapi.entity.adminEntity.BasicSetting.BasicReadSetting;

import com.dqy.englishstudyapi.tablebean.Ciku;
import com.dqy.englishstudyapi.tablebean.Read;
import com.dqy.englishstudyapi.tablebean.Readtypesub;
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
public class ReadSimpleFull {
    Readtypesub readtypesub;
    Integer count;
    String parentDsc;
}
