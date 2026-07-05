package com.dqy.englishstudyapi.generator;

import com.dqy.englishstudyapi.util.ListUtil;
import com.dqy.englishstudyapi.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


public class Test {

    public static void main(String[] args) {
//        String abc = "asbsasdasdassdasdas,asdasdsdasd，啊实打实asda.撒旦大苏打阿松大？阿三大苏打阿松大飒飒的!a十大?";
//        String[] abs = abc.split("(?<=[.?!？])");
//        for (int i = 0; i <abs.length ; i++) {
//            System.out.println(abs[i]);
//        }

       RandomUtil randomUtil = new RandomUtil();
        System.out.println(randomUtil.getRandomRange(0,2));

    }
}
