package com.dqy.englishstudyapi.tablebean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Repository
public class CikuN {
    Integer id;
    String initial;
    Integer wid;
    Timestamp createtime;
    Timestamp updatetime;
}
