package com.dqy.englishstudyapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Repository
public class SubReturnVo {
    boolean result;
    String message;
    Integer code;
    Object data;
}
