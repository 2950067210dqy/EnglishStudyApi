package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ListUtil {
    public final char[] abs={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    //将list按首字母分类
    public HashMap<Character, ArrayList<String>> stringListClassifyByInitial(ArrayList<String> list){
        HashMap<Character, ArrayList<String>> result = getEmptyStringListClassifyByInitial();
        for (String str:
             list) {
            Character initial = str.charAt(0);
            //转成小写
            if (initial<'a'){
                initial=(char) ((int)initial+32);
            }
            result.get(initial).add(str);
        }

        return  result;
    }

    //得到空的 分类的hashmap  {'a':[],'b':[],'c':[].......}
    public HashMap<Character, ArrayList<String>> getEmptyStringListClassifyByInitial(){
        HashMap<Character, ArrayList<String>> result = new HashMap<Character, ArrayList<String>>();
        for (Character initial:
             abs) {
            result.put(initial,new ArrayList<String>());
        }

        return result;
    }
}
