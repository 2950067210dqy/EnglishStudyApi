package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WordUtil {

    public ArrayList<String> filter(ArrayList<String> words){
        ArrayList<String> newWords = new ArrayList<>();
        for (String word:words
             ) {
            word=filterNotation(word);
            word=filterNumber(word);
            newWords.add(word);
        }
        return newWords;
    }

    public String filter(String word){
        word=filterNotation(word);
        word=filterNumber(word);
        return word;
    }


    public String filterNumber (String word){
        word=word.replace("1","");
        word=word.replace("2","");
        word=word.replace("3","");
        word=word.replace("4","");
        word=word.replace("5","");
        word=word.replace("6","");
        word=word.replace("7","");
        word=word.replace("8","");
        word=word.replace("9","");
        word=word.replace("0","");
        return  word;
    }

    public String filterNotation (String word){
        word=word.replace("\"","");
        word=word.replace("'","");
        word=word.replace(".","");
        word=word.replace("?","");
        word=word.replace("!","");
        word=word.replace("@","");
        word=word.replace("！","");
        word=word.replace("？","");
        word=word.replace("。","");
        word=word.replace("，","");
        word=word.replace(",","");
        word=word.replace("“","");
        word=word.replace("‘","");
        word=word.replace("《","");
        word=word.replace("》","");
        word=word.replace("【","");
        word=word.replace("】","");
        word=word.replace("{","");
        word=word.replace("}","");
        word=word.replace("<","");
        word=word.replace(">","");
        word=word.replace("[","");
        word=word.replace("]","");
        return  word;
    }

}
