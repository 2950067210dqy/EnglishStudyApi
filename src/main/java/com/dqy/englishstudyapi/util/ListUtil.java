package com.dqy.englishstudyapi.util;

import com.dqy.englishstudyapi.entity.endEntity.ReviewEnd;
import com.dqy.englishstudyapi.entity.endEntity.WordSimpleEnd;
import com.dqy.englishstudyapi.tablebean.Cikuexample;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ListUtil<T> {
    public final char[] abs={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public HashMap<Character, ArrayList<ReviewEnd>> reviewEndListClassifyByInitial(ArrayList<ReviewEnd> list){
        HashMap<Character, ArrayList<ReviewEnd>> result = getEmptyReviewEndListClassifyByInitial();
        for (ReviewEnd w:
                list) {
            Character initial = w.getInitial().trim().charAt(0);
            //转成小写
            if (initial<'a'){
                initial=(char) ((int)initial+32);
            }
            result.get(initial).add(w);
        }

        return  result;
    }




    public HashMap<Character, ArrayList<WordSimpleEnd>> wordSimpleEndListClassifyByInitial(ArrayList<WordSimpleEnd> list){
        HashMap<Character, ArrayList<WordSimpleEnd>> result = getEmptyWordSimpleEndListClassifyByInitial();
        for (WordSimpleEnd w:
                list) {
            Character initial = w.getInitial().trim().charAt(0);
            //转成小写
            if (initial<'a'){
                initial=(char) ((int)initial+32);
            }
            result.get(initial).add(w);
        }

        return  result;
    }

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

    public HashMap<Character, ArrayList<Cikuexample>> cikuexampleListClassifyByInitial(ArrayList<Cikuexample> cikuexamples ) {
        HashMap<Character, ArrayList<Cikuexample>> result = getEmptyCikuexampleListClassifyByInitial();
        for (Cikuexample cikuexample:
                cikuexamples) {
            Character initial = cikuexample.getInitial().trim().charAt(0);
            //转成小写
            if (initial<'a'){
                initial=(char) ((int)initial+32);
            }
            result.get(initial).add(cikuexample);
        }

        return  result;
    }

    //得到空的 分类的hashmap  {'a':[],'b':[],'c':[].......}
    public HashMap<Character, ArrayList<WordSimpleEnd>> getEmptyWordSimpleEndListClassifyByInitial(){
        HashMap<Character, ArrayList<WordSimpleEnd>> result = new HashMap<Character, ArrayList<WordSimpleEnd>>();
        for (Character initial:
                abs) {
            result.put(initial,new ArrayList<WordSimpleEnd>());
        }

        return result;
    }

    //得到空的 分类的hashmap  {'a':[],'b':[],'c':[].......}
    public HashMap<Character, ArrayList<Cikuexample>> getEmptyCikuexampleListClassifyByInitial(){
        HashMap<Character, ArrayList<Cikuexample>> result = new HashMap<Character, ArrayList<Cikuexample>>();
        for (Character initial:
                abs) {
            result.put(initial,new ArrayList<Cikuexample>());
        }

        return result;
    }
    public HashMap<Character, ArrayList<ReviewEnd>> getEmptyReviewEndListClassifyByInitial() {
        HashMap<Character, ArrayList<ReviewEnd>> result = new HashMap<Character, ArrayList<ReviewEnd>>();
        for (Character initial:
                abs) {
            result.put(initial,new ArrayList<ReviewEnd>());
        }

        return result;
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
