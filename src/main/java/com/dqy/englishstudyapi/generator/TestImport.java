package com.dqy.englishstudyapi.generator;

import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.TimeUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TestImport {

    //驱动名称(包含了数据库的产品和数据库的版本号)
    private static String driver = "com.mysql.cj.jdbc.Driver";
    //数据库的url
    private static String url = "jdbc:mysql://localhost:3306/englishstudy?&serverTimezone=Asia/Shanghai";
    //数据库用户名
    private static String user = "root";


    //数据库密码
    private static String  pass = "";
    private static ArrayList<Word> words = new ArrayList<Word>();
    private static HashMap<String,String> maps = new HashMap<>();
    private static TimeUtil timeUtil = new TimeUtil();
    private static String[] sss= new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private static int count =0;
    static {


        maps.put("a","abbr.[缩写词]");
        maps.put("b","adj.[形容词]");
        maps.put("c","adv.[副词]");
        maps.put("d","art.[冠词]");
        maps.put("e","aux.[助动词]");
        maps.put("f","conj.[连接词]");
        maps.put("g","int.[语气词]");
        maps.put("h","interj.[感叹词]");
        maps.put("i","n.[名词]");
        maps.put("j","num.[数词]");
        maps.put("k","prep.[介系词]");
        maps.put("l","pron.[代名词]");
        maps.put("m","v.[动词]");
        maps.put("n","vi.[不及物动词]");
        maps.put("o","vt.[及物动词]");
        maps.put("p","oth.[其它词]");

    };
    //获取数据库连接
    private static  Connection conn;
    private static   Statement statement;


    static {
        //加载mysql驱动
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url,user,pass);
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //使用jdbc进行添加操作
    public static void select(String tablesuffix) throws Exception {

        //创建Statement


        for (Word w:words
             ) {
            //编写sql语句
//            String sql= "insert into word_"+tablesuffix+ "  (id,word,charac,trans,soundmark1,soundmark2,deleted,createtime,updatetime)  values (NULL,\""+w.getWord()+"\",'"+w.getCharac()+"',\""+w.getTrans()+"\",\""+w.getSoundmark1()+"\",\""+w.getSoundmark2()+"\",0,'"+w.getCreatetime()+"','"+w.getCreatetime()+"');";
            String sql=  " insert into word (id,word,charac,trans,soundmark1,soundmark2,deleted,createtime,updatetime)  values (NULL,\""+w.getWord()+"\",'"+w.getCharac()+"',\""+w.getTrans()+"\",\""+w.getSoundmark1()+"\",\""+w.getSoundmark2()+"\",0,'"+w.getCreatetime()+"','"+w.getCreatetime()+"');  \n";
  //         String sql= "DROP TABLE `word_"+s+"`;";
//            String sql= "CREATE TABLE `englishstudy`.`word_"+s+"` (\n" +
//                    "                                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,\n" +
//                    "                                       `word` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,\n" +
//                    "                                       `charac` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\n" +
//                    "                                       `trans` VARCHAR(1500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,\n" +
//                    "                                       `soundmark1` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,\n" +
//                    "                                       `soundmark2` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,\n" +
//                    "                                        `deleted` INT( 2 ) NOT NULL DEFAULT '0',\n" +
//                    "                                       `createtime` DATETIME NOT NULL,\n" +
//                    "                                       `updatetime` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,\n" +
//                    "                                       INDEX ( `word` ,`charac` ,`updatetime` ,`deleted`)\n" +
//                    ") ENGINE = INNODB CHARACTER SET utf8 COLLATE utf8_general_ci;";
//            System.out.println(sql);
            System.out.println(sql);
            count += statement.executeUpdate(sql);
            System.out.println("执行sql成功，一共影响了"+count+"条数据");
        }


//        System.out.println(sql);
        //执行sql语句


    }
    public static void read() throws Exception {

        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("C:\\WorkSpace\\JavaProject\\EnglishStudyApi\\src\\main\\java\\com\\dqy\\englishstudyapi\\generator\\bestVab.txt"),"UTF-16"));
            String line = reader.readLine();
            String base="a";
            while (line != null) {


//                System.out.println(line);
                if (line.indexOf("#")>-1){
                    line=line.replace("\\","");
                    String word =line.substring(line.indexOf("#")+1,line.indexOf("$"));
                    String newBase =word.substring(0,1).toLowerCase().trim();
                    if (!base.equals(newBase)){
                        //数据库

                        select(base);

                        base=newBase;
                        words = new ArrayList<>();
                    }
                    String soundmark1=line.substring(line.indexOf("[")+1,line.indexOf("]"))  ;
                    String str2=line.substring(line.indexOf("]")+1);

                    if (str2.substring(0,1).trim().equals("]")){
                       str2=str2.substring(1);
                    }
                    String   soundmark2=str2.substring(str2.indexOf("[")+1,str2.indexOf("]"));
                    str2 =str2.substring(str2.indexOf(">"));
//                    String charac=str2.substring(str2.indexOf(">")+1,str2.indexOf("^"));
//                    String trans = str2.substring(str2.indexOf("^")+1).trim();
                    String charac  ="";
                    String trans = str2.substring(str2.indexOf(">")).trim();
                    while (trans.indexOf('>')>=0){
                        String tcharac=trans.substring(trans.indexOf(">")+1,trans.indexOf("^"));
                        if (!charac.contains(maps.get(tcharac))){
                            charac+=maps.get(tcharac);
                        }

                        if (trans.indexOf('>')==0){
                            trans=maps.get(tcharac)+trans.substring(trans.indexOf("^")+1);
                        }else{
                            trans=trans.substring(0,trans.indexOf(">"))+maps.get(tcharac)+trans.substring(trans.indexOf("^")+1);
                        }

                    }
                    trans = trans.replace("\"","'");
                    if (trans.indexOf("@")>0){
                        trans=trans.substring(0,trans.indexOf("@"));

                    }

//                    System.out.println(trans);
                    Word word1 = new Word();
                    word1.setWord(word);
                    word1.setCharac(charac);
                    word1.setSoundmark1(soundmark1);
                    word1.setSoundmark2(soundmark2);
                    word1.setTrans(trans);
                    word1.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    words.add(word1);

                }

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //测试类的主方法
    public static void main(String[] args) throws Exception {
      // select("");
        //先获取程序运行前的总毫秒数
        long before = System.currentTimeMillis();
        Date bd=timeUtil.getCurrentTimeDate();

       read();
        //释放资源(建议先判断statement和conn是否为空再进行关闭)
        if(statement != null){
            statement.close();
        }
        if(conn != null){
            conn.close();
        }
        //再获取程序执行完毕的总毫秒数
        long later = System.currentTimeMillis();
        Date bl=timeUtil.getCurrentTimeDate();
        //输出两者之差 得到程序执行的时间
        System.out.println(bd.toString());
        System.out.println(bl.toString());
        System.out.println((later - before)/1000.f/60.f+"分钟");

    }



}
