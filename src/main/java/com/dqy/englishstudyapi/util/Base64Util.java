package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Base64Util {
    final Base64.Decoder decoder = Base64.getDecoder();
    final Base64.Encoder encoder = Base64.getEncoder();
    public String encodeToString(String str){
        return  encoder.encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
    public String decodeToString(String str){
        return  new String(decoder.decode(str),StandardCharsets.UTF_8);
    }

}
