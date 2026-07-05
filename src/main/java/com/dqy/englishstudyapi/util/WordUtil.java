package com.dqy.englishstudyapi.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WordUtil {

    public List<String> spilitSentence(String essay){
        String[] abs = essay.split("(?<=[.?!？;；。！])");
        List<String> sentence = new ArrayList<>();
        sentence=  Arrays.asList(abs);
        return  sentence;
    }
    public List<String> spilitComma(String essay){
        String[] abs = essay.split("(?<=[,，])");
        List<String> sentence = new ArrayList<>();
        sentence=  Arrays.asList(abs);
        return  sentence;
    }
    public List<String> spilitBlank(String essay){
        String[] abs = essay.split("(?<=[  ])");
        List<String> sentence = new ArrayList<>();
        sentence=  Arrays.asList(abs);
        return  sentence;
    }
    public ArrayList<String> filter(List<String> words){
        ArrayList<String> newWords = new ArrayList<>();
        for (String word:words
             ) {
            word=filter(word);
            word=replaceChinese(word);

            if (word.length()!=0&&!word.equals("")){
                if (word.substring(0,1).equals("-")){
                    word=word.replace("-","");
                }
                newWords.add(word);
            }

        }
        return newWords;
    }

    public String filter(String word){
        word=replaceBlank(word);
        word=replaceLineBreak(word);
        word=filterNotation(word);
        word=filterNumber(word);
        return word;
    }

    private String replaceLineBreak(String word) {
        return word.replace("\n","");
    }

    public  String replaceBlank(String word){
     String word2 =word.replace(" ","");
     word2=word2.replace(" " ,"");
     word2= word2.trim();
     return  word2;
    }
    public String replaceChinese(String word){
        return  filterNotation(word.replaceAll("[\u4e00-\u9fa5]","" ));
    }
    public String replaceEnglish(String word){
        return  word.replaceAll("[a-zA-Z]","" );
    }
    public String replaceNumber(String word){
        return  word.replaceAll("\\d+","");
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
        word=word.replace("\\","");
        word=word.replace("/","");
        word=word.replace("·","");
        word=word.replace("——","");
        word=word.replace("_","");
        word=word.replace("|","");
        word=word.replace(";","");
        word=word.replace("；","");
        word=word.replace("：","");
        word=word.replace(":","");
        word=word.replace("+","");

        word=word.replace("#","");
        word=word.replace("$","");
        word=word.replace("￥","");
        word=word.replace("%","");
        word=word.replace("^","");
        word=word.replace("&","");
        word=word.replace("*","");
        word=word.replace("*","");
        return  word;
    }
    public boolean isContainChinese(String str)  {


        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
