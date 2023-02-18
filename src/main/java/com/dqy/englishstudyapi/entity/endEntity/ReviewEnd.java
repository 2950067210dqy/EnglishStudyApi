package com.dqy.englishstudyapi.entity.endEntity;

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
public class ReviewEnd {
    Integer id;
    String initial;
    LocalDateTime updatetime;
}
