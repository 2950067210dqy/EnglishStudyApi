package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {
    private  final String BASIC1 = "123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private  final String BASIC2 = "1234567890";
    public String randomAll(Integer length) {
        char[] basicArray = BASIC1.toCharArray();
        Random random = new Random();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            int index = random.nextInt(100) % (basicArray.length);
            result[i] = basicArray[index];
        }
        return new String(result);
    }
    public String randomNumber(Integer length) {
        char[] basicArray = BASIC2.toCharArray();
        Random random = new Random();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            int index = random.nextInt(100) % (basicArray.length);
            result[i] = basicArray[index];
        }
        return new String(result);
    }

    public Integer getRandomRange(Integer start,Integer end){
        if (start>end){
            Integer flag =start;
            start= end;
            end=flag;
        }
        Random random = new Random();
        Integer result = random.nextInt(end - start + 1) + start;
        return result;
    }
}
