package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {
    private  final String BASIC = "123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public String random() {
        char[] basicArray = BASIC.toCharArray();
        Random random = new Random();
        char[] result = new char[6];
        for (int i = 0; i < result.length; i++) {
            int index = random.nextInt(100) % (basicArray.length);
            result[i] = basicArray[index];
        }
        return new String(result);
    }

}
