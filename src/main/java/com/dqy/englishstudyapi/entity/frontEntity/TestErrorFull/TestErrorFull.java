package com.dqy.englishstudyapi.entity.frontEntity.TestErrorFull;

import com.dqy.englishstudyapi.entity.frontEntity.TestFront;
import com.dqy.englishstudyapi.tablebean.Testerror;
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
public class TestErrorFull {
    Testtype testtype;
    TestFront testFront;
    Testerror testerror;
}

