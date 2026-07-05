package com.dqy.englishstudyapi.entity.frontEntity;

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
public class TestTypeFront {
    Testtype testtype;
    Integer count;

}
