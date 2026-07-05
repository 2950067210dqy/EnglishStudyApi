package com.dqy.englishstudyapi.entity.frontEntity.TestLikeFull;

import com.dqy.englishstudyapi.entity.frontEntity.TestFront;
import com.dqy.englishstudyapi.tablebean.Testlike;
import com.dqy.englishstudyapi.tablebean.Testtype;
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
public class TestLikeFull {
    Testtype testtype;
    TestFront testFront;
    Testlike testlike;
}
