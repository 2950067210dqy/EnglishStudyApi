package com.dqy.englishstudyapi.entity.frontEntity;

import com.dqy.englishstudyapi.tablebean.Test;
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
public class TestFront {
    Test test;
    Boolean isLike;
}
