package com.dqy.englishstudyapi.entity.frontEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
@ToString
public class WxReturnUser {
    String openid;
    String session_key;
    String unionid	;
    Integer errcode;
    String errmsg	;
}
