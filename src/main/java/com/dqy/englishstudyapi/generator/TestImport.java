package com.dqy.englishstudyapi.generator;

import com.alibaba.fastjson.JSONObject;
import com.dqy.englishstudyapi.entity.frontEntity.CikuWord;
import com.dqy.englishstudyapi.tablebean.Word;
import com.dqy.englishstudyapi.util.FileUtil;
import com.dqy.englishstudyapi.util.TimeUtil;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
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
    private static ArrayList<String> wordsEasy = new ArrayList<String>();
    private static HashMap<String,String> maps = new HashMap<>();
    private static TimeUtil timeUtil = new TimeUtil();
    private static FileUtil fileUtil = new FileUtil();
    private static boolean flags=true;
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


        for (Word  w:words
             ) {
            //编写sql语句
        //    String sql= "insert into word_"+tablesuffix+ "  (id,word,charac,trans,soundmark1,soundmark2,deleted,createtime,updatetime)  values (NULL,\""+w.getWord()+"\",'"+w.getCharac()+"',\""+w.getTrans()+"\",\""+w.getSoundmark1()+"\",\""+w.getSoundmark2()+"\",0,'"+w.getCreatetime()+"','"+w.getCreatetime()+"');";
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
                    System.out.println(word);
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
                        select(newBase);

                        base=newBase;
                        words = new ArrayList<>();
                    }

//                    System.out.println(trans);
                    Word word1 = new Word();
                    word1.setWord(word);
                    word1.setCharac(charac);
                    word1.setSoundmark1(soundmark1);
                    word1.setSoundmark2(soundmark2);
                    word1.setTrans(trans);
                    word1.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
                    if (word.equals("yurt")){
                        flags=true;
                    }
                    if (flags){
//                        System.out.println(word);
                        words.add(word1);
                    }

                    //最后Z的单词
                    if (trans.indexOf("@")>0){
                        select(newBase);
                        base=newBase;
                        words = new ArrayList<>();
                    }
                }

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  void releaseConn() throws Exception{
        //释放资源(建议先判断statement和conn是否为空再进行关闭)
        if(statement != null){
            statement.close();
        }
        if(conn != null){
            conn.close();
        }
    }
    public static void testImportWords() throws Exception {
        // select("");
        //先获取程序运行前的总毫秒数
        long before = System.currentTimeMillis();
        Date bd=timeUtil.getCurrentTimeDate();

        read();


        //再获取程序执行完毕的总毫秒数
        long later = System.currentTimeMillis();
        Date bl=timeUtil.getCurrentTimeDate();
        //输出两者之差 得到程序执行的时间
        System.out.println(bd.toString());
        System.out.println(bl.toString());
        System.out.println((later - before)/1000.f/60.f+"分钟");
    }

    //测试类的主方法
    public static void main(String[] args) throws Exception {
//     testImportWords();
       testImportProunce();
//select("");
        releaseConn();
    }
    private static void testImportProunce() throws Exception {
        //先获取程序运行前的总毫秒数
        long before = System.currentTimeMillis();
        Date bd=timeUtil.getCurrentTimeDate();
//        read2();
//        read3();

        //  read4();
        read5();
        //再获取程序执行完毕的总毫秒数
        long later = System.currentTimeMillis();
        Date bl=timeUtil.getCurrentTimeDate();
        //输出两者之差 得到程序执行的时间
        System.out.println(bd.toString());
        System.out.println(bl.toString());
        System.out.println((later - before)/1000.f/60.f+"分钟");
    }
    //从 数据分表导入合表
    private static void read5() throws Exception {

        String jsonstr="{\"UnMasteredWords\":[\"unite\",\"economist\",\"turkey\",\"data\",\"rate\",\"case\",\"local\",\"settlement\",\"concrete\",\"online\",\"quantum\",\"central\",\"fora\",\"tax\",\"tech\",\"claim\",\"litigation\",\"reform\",\"minister\",\"fund\",\"system\",\"political\",\"prince\",\"union\",\"nance\",\"royal\",\"limit\",\"global\",\"election\",\"infrastructure\",\"despite\",\"zone\",\"issue\",\"digital\",\"credit\",\"promise\",\"carbon\",\"individual\",\"report\",\"liberal\",\"commission\",\"chase\",\"meanwhile\",\"private\",\"crisis\",\"media\",\"average\",\"raise\",\"insurer\",\"industry\",\"crime\",\"source\",\"deal\",\"republican\",\"sex\",\"publish\",\"measure\",\"provide\",\"legal\",\"search\",\"rise\",\"instrument\",\"structure\",\"deutsche\",\"regime\",\"personal\",\"abuse\",\"region\",\"prime\",\"scheme\",\"process\",\"base\",\"blame\",\"decade\",\"defence\",\"mate\",\"lawsuit\",\"response\",\"expect\",\"reduce\",\"campaign\",\"treaty\",\"charge\",\"announce\",\"approach\",\"be-tween\",\"release\",\"serve\",\"general\",\"balance\",\"production\",\"nation\",\"border\",\"boom\",\"software\",\"platform\",\"institute\",\"emerge\",\"bomb\",\"intelligence\",\"potential\",\"million\",\"design\",\"marriage\",\"fee\",\"administration\",\"subscription\",\"evidence\",\"construction\",\"technology\",\"account\",\"cash\",\"reinforce\",\"damage\",\"federal\",\"build\",\"swap\",\"struggle\",\"invest\",\"statue\",\"reserve\",\"powerful\",\"range\",\"investment\",\"revenue\",\"bar\",\"setup\",\"genome\",\"kingdom\",\"appeal\",\"startup\",\"control\",\"economy\",\"canyon\",\"payment\",\"recession\",\"support\",\"power\",\"exist\",\"piece\",\"pro\",\"workload\",\"tonne\",\"race\",\"accuse\",\"remove\",\"role\",\"due\",\"term\",\"exchange\",\"lawyer\",\"cation\",\"bene\",\"initiative\",\"crop\",\"chart\",\"indeed\",\"agreement\",\"coalition\",\"user\",\"economic\",\"leadership\",\"approve\",\"citizen\",\"culture\",\"abandon\",\"advance\",\"declare\",\"tip\",\"product\",\"migrant\",\"freedom\",\"pain\",\"device\",\"theory\",\"collapse\",\"deny\",\"press\",\"sow\",\"rival\",\"relative\",\"tie\",\"court\",\"classical\",\"democrat\",\"crack\",\"torture\",\"institution\",\"survey\",\"organisation\",\"port\",\"voter\",\"peninsula\",\"bond\",\"governor\",\"workin\",\"period\",\"growth\",\"asset\",\"complain\",\"website\",\"threat\",\"selection\",\"missile\",\"reward\",\"resist\",\"long-term\",\"competition\",\"reveal\",\"customer\",\"panel\",\"require\",\"submit\",\"sin\",\"spread\",\"estimate\",\"concern\",\"fuel\",\"handful\",\"weapon\",\"trademark\",\"opt\",\"worse\",\"activist\",\"property\",\"authority\",\"elite\",\"associate\",\"mill\",\"extreme\",\"odd\",\"failing\",\"currency\",\"referendum\",\"province\",\"sentence\",\"parrot\",\"conduct\",\"hunt\",\"brand\",\"strike\",\"channel\",\"respond\",\"manufacturing\",\"co-operate\",\"fraction\",\"curb\",\"retail\",\"internal\",\"stem\",\"contract\",\"award\",\"metal\",\"cult\",\"impact\",\"pipeline\",\"candidate\",\"nevertheless\",\"maintenance\",\"destroy\",\"equity\",\"accord\",\"re-education\",\"partner\",\"acute\",\"course\",\"version\",\"corporate\",\"impose\",\"vital\",\"demand\",\"gulf\",\"opposition\",\"material\",\"scare\",\"transfer\",\"save\",\"progressive\",\"corruption\",\"prisoner\",\"tend\",\"convince\",\"prove\",\"poll\",\"dominate\",\"favour\",\"proof\",\"alcohol\",\"secretary\",\"bold\",\"roughly\",\"debt\",\"civil\",\"aid\",\"default\",\"physical\",\"faith\",\"failure\",\"option\",\"deprive\",\"shame\",\"cure\",\"wild\",\"arrest\",\"stock\",\"coup\",\"ideological\",\"trial\",\"jury\",\"energy\",\"register\",\"historical\",\"consultancy\",\"strategy\",\"pressure\",\"risky\",\"senate\",\"drain\",\"veteran\",\"reverse\",\"replace\",\"regional\",\"unlike\",\"conventional\",\"subsidiary\",\"county\",\"resign\",\"swamp\",\"politics\",\"tackle\",\"pledge\",\"capitalism\",\"economics\",\"argument\",\"protection\",\"ally\",\"rural\",\"ministry\",\"propose\",\"index\",\"industrial\",\"indicator\",\"judge\",\"suspend\",\"stress\",\"sign\",\"persuade\",\"unfortunately\",\"detail\",\"sector\",\"responsibility\",\"timely\",\"coca\",\"directly\",\"valid\",\"artist\",\"clerical\",\"tweet\",\"ban\",\"obtain\",\"jail\",\"ratio\",\"previous\",\"rescue\",\"gluten\",\"history\",\"republic\",\"distance\",\"strengthen\",\"unknown\",\"nationwide\",\"eligible\",\"portfolio\",\"slowdown\",\"sexual\",\"critical\",\"dreadful\",\"publication\",\"assert\",\"fentanyl\",\"reap\",\"manufacturer\",\"reckon\",\"breed\",\"appoint\",\"geography\",\"nota\",\"intermediary\",\"excellence\",\"generation\",\"electoral\",\"commercial\",\"hostage\",\"align\",\"consequence\",\"client\",\"button\",\"storage\",\"comp\",\"specialise\",\"capture\",\"catch\",\"explode\",\"totalitarian\",\"combination\",\"export\",\"complacency\",\"potentially\",\"stage\",\"rattle\",\"entitle\",\"breakdown\",\"docile\",\"lockup\",\"pessimism\",\"link\",\"solve\",\"replica\",\"strait\",\"impeach\",\"crackdown\",\"row\",\"rand\",\"attract\",\"levy\",\"anti-corruption\",\"playa\",\"junk\",\"launch\",\"taxman\",\"unpredictable\",\"continent\",\"destabilise\",\"hectare\",\"stake\",\"site\",\"suspension\",\"burden\",\"gap\",\"route\",\"ensure\",\"perpetrate\",\"premium\",\"execute\",\"mobile\",\"lobbyist\",\"tendon\",\"doubtful\",\"recommend\",\"to-day\",\"database\",\"urge\",\"ancient\",\"jam\",\"curtain\",\"discussion\",\"error\",\"permission\",\"extra\",\"certainly\",\"hence\",\"plaintiff\",\"real-world\",\"devolve\",\"rape\",\"clinic\",\"prosperity\",\"painful\",\"beach\",\"compliance\",\"wrongdoing\",\"cardinal\",\"sacred\",\"view\",\"bonito\",\"lacklustre\",\"wipe\",\"affiliate\",\"settle\",\"stream\",\"committee\",\"serfdom\",\"pace\",\"cream\",\"strain\",\"percentage\",\"apply\",\"facsimile\",\"enterprise\",\"program\",\"abroad\",\"expel\",\"suspect\",\"capable\",\"constitutional\",\"slap\",\"know-how\",\"fighter\",\"select\",\"principal\",\"liable\",\"scandal\",\"shortcoming\",\"league\",\"camera\",\"migration\",\"outcome\",\"urban\",\"stable\",\"domestic\",\"aluminium\",\"expense\",\"casual\",\"ate\",\"rent\",\"brutal\",\"label\",\"lament\",\"technique\",\"rare\",\"earn\",\"fashion\",\"endeavour\",\"relation\",\"absence\",\"volume\",\"periodical\",\"competitive\",\"legislative\",\"isolation\",\"scale\",\"romance\",\"tandem\",\"detect\",\"charity\",\"traditional\",\"package\",\"transition\",\"turmoil\",\"discount\",\"hell\",\"male\",\"threaten\",\"exposure\",\"argue\",\"appearance\",\"implicate\",\"crash\",\"purchase\",\"costa\",\"navigate\",\"describe\",\"survive\",\"cocaine\",\"insurance\",\"reputation\",\"possibility\",\"pilot\",\"seize\",\"centralise\",\"proposal\",\"society\",\"golf\",\"roundup\",\"household\",\"capriciousness\",\"predictably\",\"delicate\",\"fury\",\"rank\",\"mechanical\",\"median\",\"ignore\",\"dislike\",\"maritime\",\"disappoint\",\"overcome\",\"processing\",\"income\",\"avenue\",\"transaction\",\"informal\",\"series\",\"grand\",\"universal\",\"innovation\",\"subject\",\"schedule\",\"dispatch\",\"crucial\",\"globally\",\"endless\",\"defendant\",\"secure\",\"surface\",\"noble\",\"explosive\",\"partial\",\"contact\",\"intend\",\"shave\",\"project\",\"addition\",\"bite\",\"buyer\",\"fuse\",\"soil\",\"complex\",\"visible\",\"mosque\",\"ostrich\",\"partisan\",\"spill\",\"rough\",\"agriculture\",\"virgin\",\"agency\",\"diminish\",\"treat\",\"spate\",\"employment\",\"automotive\",\"tinder\",\"penalty\",\"deviate\",\"behave\",\"colleague\",\"break-up\",\"super\",\"engineering\",\"detention\",\"happiness\",\"bean\",\"expand\",\"liberty\",\"tilt\",\"community\",\"decline\",\"electronic\",\"postmaster\",\"vote\",\"intellectual\",\"shell\",\"calm\",\"tighten\",\"pastor\",\"crowd\",\"lighten\",\"desire\",\"stern\",\"revolution\",\"brief\",\"fake\",\"virtual\",\"combat\",\"moderate\",\"trypsin\",\"chain\",\"elect\",\"pursue\",\"lifetime\",\"transport\",\"estate\",\"short-term\",\"detain\",\"immigration\",\"position\",\"produce\",\"budget\",\"contrarian\",\"tunnel\",\"vocational\",\"haulage\",\"loan\",\"stime\",\"amid\",\"footage\",\"tale\",\"ugly\",\"compute\",\"finance\",\"principle\",\"darkness\",\"forensic\",\"expansion\",\"relieve\",\"stretch\",\"rogue\",\"commerce\",\"seller\",\"display\",\"error-prone\",\"application\",\"slash\",\"amazon\",\"automation\",\"victim\",\"wage\",\"establish\",\"renovation\",\"overwhelm\",\"nationalism\",\"minimum\",\"tone\",\"passive\",\"predictable\",\"entity\",\"promote\",\"weakness\",\"presidential\",\"investigate\",\"on-line\",\"chamber\",\"undermine\",\"refuse\",\"feature\",\"opposite\",\"acquire\",\"poverty\",\"relate\",\"independence\",\"demonstrate\",\"surge\",\"stare\",\"govern\",\"copyright\",\"impound\",\"transmit\",\"colonisation\",\"touse\",\"conform\",\"retaliate\",\"vulnerable\",\"authoritarianism\",\"carpet\",\"jewelled\",\"irrigation\",\"strength\",\"tear\",\"vintage\",\"unexpected\",\"horrible\",\"moat\",\"spirit\",\"investigator\",\"cancellation\",\"aim\",\"over-the-counter\",\"transparency\",\"concentration\",\"troubadour\",\"oversight\",\"inevitable\",\"laden\",\"squabble\",\"penultimate\",\"provider\",\"peer\",\"gutsy\",\"blow\",\"overseas\",\"millionaire\",\"paralyse\",\"remedy\",\"recover\",\"track\",\"manpower\",\"nuclear\",\"desperation\",\"essential\",\"tabloid\",\"grave\",\"inquest\",\"developer\",\"ancestral\",\"previously\",\"digest\",\"vocation\",\"privately\",\"susceptible\",\"windfall\",\"restrict\",\"heron\",\"heroine\",\"disarm\",\"python\",\"cautious\",\"handle\",\"fortune\",\"hole\",\"entrance\",\"grandee\",\"close-up\",\"provoke\",\"allege\",\"iron\",\"monetary\",\"foolish\",\"infuriate\",\"enlist\",\"brutality\",\"murphy\",\"civilisation\",\"annual\",\"forcible\",\"document\",\"temperamental\",\"professional\",\"enrol\",\"laptop\",\"roam\",\"throne\",\"minuscule\",\"smuggler\",\"carve\",\"athlete\",\"triangle\",\"pathos\",\"escape\",\"shunt\",\"backdown\",\"reimbursement\",\"missionary\",\"kenyan\",\"blanket\",\"basilica\",\"shelter\",\"experimentation\",\"diet\",\"embargo\",\"hip\",\"severity\",\"delay\",\"tutor\",\"metrics\",\"once-in-a-lifetime\",\"perfume\",\"congress\",\"contribution\",\"paradoxically\",\"ultimately\",\"pierce\",\"navy\",\"opinion\",\"counterpart\",\"vignette\",\"retaliatory\",\"exception\",\"cooperate\",\"variant\",\"aground\",\"dioxide\",\"anti-discrimination\",\"wavelength\",\"justify\",\"negotiator\",\"waive\",\"involve\",\"reliability\",\"languish\",\"portray\",\"sauce\",\"unleash\",\"spruce\",\"farce\",\"accurate\",\"loyalty\",\"pension\",\"chastise\",\"derivation\",\"highly\",\"department\",\"slack\",\"statute\",\"vigil\",\"accelerator\",\"frankfurt\",\"implication\",\"motherhood\",\"unacceptable\",\"potassium\",\"muddy\",\"mitigate\",\"boon\",\"transylvania\",\"restore\",\"supporter\",\"mere\",\"statement\",\"sedition\",\"hub\",\"advocate\",\"curious\",\"unfortunate\",\"kinship\",\"analytic\",\"generate\",\"untested\",\"in-house\",\"event\",\"particularly\",\"dump\",\"rug\",\"regulate\",\"waistcoat\",\"couch\",\"approval\",\"parallel\",\"neatness\",\"auto\",\"widen\",\"franchise\",\"spare\",\"drone\",\"producer\",\"moving\",\"entire\",\"imperial\",\"academic\",\"deceptive\",\"contagious\",\"variable\",\"prominently\",\"superstition\",\"unwarranted\",\"painter\",\"expose\",\"longtime\",\"distraction\",\"scrap\",\"observe\",\"refugee\",\"acknowledge\",\"resource\",\"cope\",\"refrain\",\"culpa\",\"canopy\",\"harvest\",\"rig\",\"activism\",\"magnetic\",\"shun\",\"caveman\",\"distribute\",\"fret\",\"cloak\",\"mass\",\"challenge\",\"regardless\",\"suit\",\"home-made\",\"unpopular\",\"leak\",\"stroke\",\"reinvigorate\",\"relegate\",\"utter\",\"privileged\",\"psychological\",\"tepid\",\"complaint\",\"neutral\",\"replacement\",\"indoctrination\",\"wave\",\"behaviour\",\"slump\",\"derivative\",\"restart\",\"betrayal\",\"dread\",\"rubble\",\"seek\",\"slum\",\"trader\",\"obvious\",\"confess\",\"hinge\",\"sublime\",\"divorce\",\"vast\",\"former\",\"scope\",\"symbolic\",\"exceptionally\",\"caprice\",\"schoolchildren\",\"incompetent\",\"shift\",\"judicial\",\"plunge\",\"desirable\",\"target\",\"mask\",\"upwards\",\"endosperm\",\"exemption\",\"conversation\",\"apart\",\"eastern\",\"actual\",\"plummet\",\"severe\",\"legitimate\",\"life-time\",\"inequality\",\"salvage\",\"divest\",\"well-intentioned\",\"homogenous\",\"yoke\",\"cruise\",\"prayer\",\"negotiate\",\"experimental\",\"valium\",\"reality\",\"weed\",\"innate\",\"consume\",\"upheaval\",\"signatory\",\"spin\",\"enable\",\"maximum\",\"caution\",\"nova\",\"opioid\",\"lawless\",\"unemployment\",\"frank\",\"mutual\",\"realm\",\"three-dimensional\",\"nationalist\",\"working-class\",\"citizenship\",\"style\",\"entertain\",\"apologise\",\"tricky\",\"bury\",\"curiously\",\"unprecedented\",\"owe\",\"swampy\",\"vanish\",\"militant\",\"bankroll\",\"revive\",\"sum\",\"doom\",\"long-range\",\"heroism\",\"deepen\",\"bizarre\",\"valuable\",\"portion\",\"disjointed\",\"trend\",\"ideal\",\"supremacy\",\"cable\",\"short-lived\",\"glow\",\"tehran\",\"counsel\",\"coincidence\",\"politician\",\"cavalier\",\"insider\",\"contravention\",\"chief\",\"comparison\",\"subsidy\",\"technical\",\"puzzle\",\"outsider\",\"equitable\",\"stabilise\",\"badly\",\"ferment\",\"faceless\",\"considerable\",\"territory\",\"mystery\",\"prophet\",\"smooth\",\"undeterred\",\"unsure\",\"risk-free\",\"prior\",\"hypothetical\",\"lurid\",\"literally\",\"illegal\",\"harbour\",\"extraordinarily\",\"payback\",\"resolve\",\"equipment\",\"limp\",\"scholar\",\"excavate\",\"gross\",\"gene\",\"malpractice\",\"sanction\",\"witness\",\"ladder\",\"polo\",\"territorial\",\"extensive\",\"bust\",\"noisy\",\"rotten\",\"appetite\",\"notion\",\"militia\",\"carbohydrate\",\"rouble\",\"quietly\",\"disguise\",\"grammar\",\"preference\",\"interactive\",\"skeletal\",\"glance\",\"bath\",\"voyage\",\"globe\",\"amylase\",\"graft\",\"modernism\",\"immediate\",\"graphic\",\"wheel\",\"allocate\",\"peaceful\",\"diocese\",\"beware\",\"subtitle\",\"overgrown\",\"correlation\",\"violence\",\"scene\",\"postage\",\"representative\",\"rail\",\"clandestine\",\"defeat\",\"wholesale\",\"crucially\",\"steep\",\"palm\",\"convention\",\"coly\",\"celebrate\",\"extraordinary\",\"manifesto\",\"creature\",\"creditor\",\"schoolboy\",\"coverage\",\"prosecution\",\"foundation\",\"incumbent\",\"beast\",\"luna\",\"reside\",\"terror\",\"laissez-faire\",\"hybridisation\",\"forever\",\"devise\",\"asylum\",\"access\",\"respectable\",\"abolition\",\"queue\",\"master\",\"situate\",\"mix\",\"breakthrough\",\"revise\",\"unlimited\",\"disaster\",\"saving\",\"pave\",\"invite\",\"sculpture\",\"sadly\",\"carnage\",\"valuation\",\"convert\",\"demur\",\"tuition\",\"extremely\",\"unseat\",\"graduate\",\"bit\",\"factor\",\"chaperone\",\"fade\",\"check-up\",\"raid\",\"alike\",\"harangue\",\"lambaste\",\"rhode\",\"variety\",\"legislation\",\"patch\",\"merge\",\"majority\",\"peak\",\"evaluate\",\"ambassador\",\"politically\",\"allergy\",\"uniform\",\"airborne\",\"litter\",\"registration\",\"boost\",\"grim\",\"tack\",\"intervention\",\"squalid\",\"reminder\",\"satellite\",\"consistently\",\"retrieval\",\"harden\",\"declaration\",\"misstep\",\"chaos\",\"accustom\",\"ottoman\",\"survival\",\"stronghold\",\"harsh\",\"inset\",\"annually\",\"archipelago\",\"accessible\",\"whirlwind\",\"chew\",\"nip\",\"threshold\",\"agile\",\"deadline\",\"harm\",\"pavilion\",\"venture\",\"additionally\",\"programmer\",\"mentor\",\"after-school\",\"fundamental\",\"autopsy\",\"co-operation\",\"discipline\",\"radical\",\"lane\",\"excrement\",\"demilitarise\",\"battle\",\"renew\",\"ramp\",\"secular\",\"corrugated\",\"operational\",\"conformity\",\"bourgeoisie\",\"equal\",\"justice\",\"backup\",\"macroeconomics\",\"acorn\",\"massage\",\"gamble\",\"guidance\",\"belief\",\"blend\",\"tin\",\"bucket\",\"heartache\",\"attorney-general\",\"removal\",\"cement\",\"nightmare\",\"macron\",\"dragon\",\"thrall\",\"sigh\",\"taiwanese\",\"convict\",\"parting\",\"shock\",\"unjust\",\"network\",\"belated\",\"attractive\",\"entrepreneur\",\"escalate\",\"safely\",\"chuckle\",\"tantrum\",\"baker\",\"patriotic\",\"corrode\",\"obsolete\",\"bookshop\",\"code\",\"outlaw\",\"propel\",\"laird\",\"unsafe\",\"electric\",\"observatory\",\"presume\",\"hindrance\",\"creed\",\"conceal\",\"legalise\",\"compensate\",\"retain\",\"unravel\",\"high-tech\",\"rectitude\",\"sober\",\"demolition\",\"shelve\",\"millennium\",\"summit\",\"bud\",\"individually\",\"surreal\",\"install\",\"leeway\",\"unique\",\"contend\",\"grab\",\"full-blown\",\"corrupt\",\"confront\",\"tyrant\",\"attack\",\"spokesman\",\"cite\",\"surround\",\"investor\",\"broadcaster\",\"well-establish\",\"bully\",\"amount\",\"expo\",\"ingenuity\",\"legend\",\"career\",\"ordnance\",\"roll\",\"additional\",\"norm\",\"gather\",\"sincerity\",\"yearly\",\"rocket\",\"desktop\",\"manipulation\",\"airstrip\",\"conclude\",\"universe\",\"successive\",\"blip\",\"clampdown\",\"bargain\",\"harness\",\"lineage\",\"cancer\",\"pick-up\",\"pathetic\",\"morale\",\"multiple\",\"session\",\"brigade\",\"thorough\",\"breach\",\"shine\",\"demonstrably\",\"commentator\",\"proportion\",\"unworkable\",\"reaction\",\"wig\",\"substance\",\"stain\",\"skip\",\"cultivation\",\"toughen\",\"wary\",\"just-in-time\",\"healthily\",\"eyewitness\",\"awe\",\"cynical\",\"journal\",\"merely\",\"collective\",\"dip\",\"frustration\",\"precaution\",\"taxon\",\"liability\",\"federation\",\"provisional\",\"pique\",\"bow\",\"astute\",\"gild\",\"dynamite\",\"warrant\",\"status\",\"aviation\",\"ward\",\"parliament\",\"respect\",\"alternative\",\"scrutinise\",\"expression\",\"taboo\",\"kindergarten\",\"atypical\",\"relationship\",\"high-level\",\"payroll\",\"cling\",\"domination\",\"collateral\",\"nickname\",\"soften\",\"consumer\",\"reader\",\"worst-case\",\"pseudonym\",\"goodwill\",\"accommodation\",\"length\",\"strive\",\"imprison\",\"concentrate\",\"cycle\",\"ultimate\",\"massive\",\"locksmith\",\"embrace\",\"bush\",\"prompt\",\"laborious\",\"eerie\",\"blockade\",\"patience\",\"license\",\"remind\",\"reformist\",\"delivery\",\"scotia\",\"persist\",\"innocent\",\"lack\",\"accusation\",\"distribution\",\"cave\",\"pursuant\",\"attendance\",\"crusade\",\"blog\",\"provincial\",\"abyss\",\"protector\",\"pinnacle\",\"palpitation\",\"amateurish\",\"security\",\"rally\",\"essay\",\"infection\",\"apprehend\",\"revoke\",\"resettle\",\"interfere\",\"adopt\",\"bishop\",\"miscalculate\",\"rubber\",\"divisive\",\"slide\",\"dial\",\"drama\",\"unharmed\",\"amain\",\"external\",\"imam\",\"engine\",\"infect\",\"anew\",\"dominance\",\"walkaway\",\"decrease\",\"high-speed\",\"request\",\"curmudgeonly\",\"foul\",\"gain\",\"profound\",\"submarine\",\"primary\",\"management\",\"abysmal\",\"express\",\"ordinary\",\"keen\",\"out-of-school\",\"skim\",\"mood\",\"matter\",\"evermore\",\"represent\",\"dividend\",\"debate\",\"disuse\",\"no-win\",\"repatriate\",\"claimant\",\"swipe\",\"statehouse\",\"emit\",\"blemish\",\"mobilise\",\"appointment\",\"incarcerate\",\"sociologist\",\"thew\",\"contention\",\"genius\",\"attempt\",\"marbled\",\"favourable\",\"bougainvillea\",\"fate\",\"ridiculous\",\"oppressor\",\"random\",\"switch\",\"instinct\",\"workforce\",\"marble\",\"sack\",\"challenger\",\"era\",\"oak\",\"infringe\",\"scarce\",\"ego\",\"prickly\",\"watchdog\",\"hitherto\",\"crookedness\",\"artery\",\"execution\",\"conference\",\"surely\",\"embolden\",\"deserve\",\"particular\",\"extend\",\"dome\",\"yield\",\"trough\",\"restrictive\",\"cease\",\"decamp\",\"dissuade\",\"punish\",\"shake\",\"run-up\",\"educational\",\"captivate\",\"emigration\",\"simulation\",\"negative\",\"evoke\",\"wildly\",\"invoice\",\"adjust\",\"reintroduction\",\"remains\",\"oppressive\",\"rebuttal\",\"sharply\",\"greenbrier\",\"overwhelmingly\",\"forgive\",\"fanatical\",\"sky-high\",\"analyst\",\"belie\",\"coeliac\",\"stunt\",\"rerun\",\"restoration\",\"holding\",\"lavish\",\"alert\",\"best-seller\",\"superior\",\"custom\",\"slip\",\"acceptable\",\"resonate\",\"hamas\",\"diversity\",\"implement\",\"mark-up\",\"advocacy\",\"stance\",\"halt\",\"doubtless\",\"twist\",\"minority\",\"fertiliser\",\"council\",\"assassinate\",\"naval\",\"vow\",\"excess\",\"structural\",\"abhor\",\"exceed\",\"bail\",\"prose\",\"ante\",\"forecast\",\"attractiveness\",\"heavyweight\",\"superb\",\"explore\",\"enlightenment\",\"uncertain\",\"telescope\",\"subside\",\"curtail\",\"sensible\",\"consent\",\"lender\",\"vigilante\",\"dent\",\"hobble\",\"cellar\",\"habit\",\"grower\",\"chill\",\"cannabis\",\"smother\",\"backlash\",\"inhabitant\",\"resilient\",\"startle\",\"epidemic\",\"consolidate\",\"movement\",\"teamwork\",\"marker\",\"self-preservation\",\"anchor\",\"attorney\",\"trumpet\",\"diplomacy\",\"suitable\",\"lure\",\"publicly\",\"appropriate\",\"admission\",\"punctuation\",\"pharmaceutical\",\"commit\",\"hassle\",\"hazy\",\"gruesome\",\"clout\",\"occurrence\",\"adaptive\",\"journalist\",\"awful\",\"heritage\",\"casualty\",\"protest\",\"content\",\"alignment\",\"heir\",\"stability\",\"peril\",\"impeachment\",\"resemble\",\"fraternity\",\"overwork\",\"viewpoint\",\"alarm\",\"year-end\",\"culprit\",\"envisage\",\"underwrite\",\"snap\",\"arbitration\",\"three-quarter\",\"condom\",\"indistinct\",\"endure\",\"discourage\",\"precipitate\",\"kerosene\",\"craftsmanship\",\"atrocity\",\"coast\",\"rely\",\"broad\",\"north-south\",\"roil\",\"waterman\",\"arcane\",\"shipbuilder\",\"end-to-end\",\"dining-room\",\"ultra-modern\",\"relocate\",\"impress\",\"photocopy\",\"blueprint\",\"sentiment\",\"disquiet\",\"mingle\",\"emission\",\"broaden\",\"withdraw\",\"parry\",\"outspoken\",\"obligation\",\"sponsor\",\"female\",\"evacuation\",\"precision\",\"barrier\",\"overtime\",\"profusion\",\"haver\",\"crore\",\"multiracial\",\"secret\",\"unimaginable\",\"provision\",\"nourish\",\"unsurprisingly\",\"warehouse\",\"crass\",\"protein\",\"frame\",\"audio\",\"dispute\",\"spectrum\",\"shoemaker\",\"paste\",\"band\",\"civilian\",\"pearl\",\"guarantee\",\"parental\",\"contain\",\"narc\",\"aggressive\",\"obelisk\",\"aggression\",\"shortfall\",\"chemical\",\"denominate\",\"unusual\",\"ruin\",\"erosion\",\"racial\",\"encase\",\"fracture\",\"spotless\",\"virtuous\",\"buoyant\",\"belong\",\"compete\",\"textbook\",\"inward\",\"injustice\",\"pickup\",\"oppose\",\"sabbatical\",\"undervalue\",\"enrichment\",\"constituency\",\"newcomer\",\"admirable\",\"swoon\",\"analyse\",\"plot\",\"retribution\",\"algorithm\",\"seasonally\",\"activity\",\"load\",\"prescribe\",\"exhaustion\",\"repression\",\"rehabilitation\",\"consortium\",\"intentionally\",\"neighbourhood\",\"chloride\",\"institutional\",\"operate\",\"fairytale\",\"reproduce\",\"stadium\",\"browser\",\"image\",\"bay\",\"creep\",\"recruit\",\"authorisation\",\"hardware\",\"boundary\",\"fruition\",\"indictment\",\"among\",\"superposition\",\"immortal\",\"leftist\",\"exempt\",\"quad\",\"outbreak\",\"crown\",\"digestible\",\"causal\",\"anti-abortion\",\"islet\",\"compensation\",\"relevance\",\"phrase\",\"distant\",\"radar\",\"pedagogy\",\"consult\",\"acritical\",\"administrative\",\"vessel\",\"methodical\",\"smallholding\",\"encounter\",\"lightweight\",\"obscure\",\"disclose\",\"premature\",\"memo\",\"proceed\",\"airwave\",\"incidence\",\"unwelcome\",\"manipulate\",\"construct\",\"twitter\",\"pin\",\"renewable\",\"predictability\",\"qualify\",\"undertake\",\"association\",\"mourn\",\"pure\",\"scramble\",\"collateralize\",\"payout\",\"frenzy\",\"pipe\",\"so-called\",\"wriggle\",\"oil-rich\",\"journey\",\"gaming\",\"childhood\",\"signal\",\"evolve\",\"volatility\",\"nil\",\"respective\",\"grumble\",\"minus\",\"courtroom\",\"overstress\",\"item\",\"bureaucracy\",\"grimy\",\"toll\",\"giant\",\"hygiene\",\"fermentation\",\"current\",\"pentagon\",\"commissar\",\"blur\",\"ambition\",\"plea\",\"liberalism\",\"undeliverable\",\"dilemma\",\"fester\",\"heed\",\"porto\",\"autonomous\",\"inadequate\",\"jurisdiction\",\"foreseeable\",\"practical\",\"inject\",\"pawn\",\"commonplace\",\"automate\",\"putt\",\"petrol\",\"bilateral\",\"gently\",\"deployment\",\"collector\",\"inexperience\",\"resurgence\",\"creation\",\"unable\",\"skin-tight\",\"nonetheless\",\"facility\",\"protracted\",\"accelerate\",\"aware\",\"warmth\",\"rethink\",\"grapple\",\"prioritise\",\"censorship\",\"matrimony\",\"stand-alone\",\"volunteer\",\"congressman\",\"akin\",\"moral\",\"edit\",\"melee\",\"singer\",\"wooden\",\"accompany\",\"regulation\",\"delightful\",\"ongoing\",\"earnings\",\"creativity\",\"judiciary\",\"ballot\",\"innovative\",\"gear\",\"congressional\",\"comparatively\",\"ethical\",\"pub\",\"pioneer\",\"oversee\",\"cell\",\"salary\",\"transportation\",\"supreme\",\"addiction\",\"landscape\",\"loss\",\"writer\",\"hemisphere\",\"temple\",\"popper\",\"pending\",\"sleeper\",\"instruction\",\"observer\",\"businessman\",\"rebrand\",\"breezy\",\"tick\",\"responsive\",\"authoritarian\",\"motorway\",\"spook\",\"criticism\",\"aboriginal\",\"delegation\",\"legislature\",\"witch\",\"scenario\",\"seaman\",\"mode\",\"lover\",\"crackpot\",\"ogre\",\"antitrust\",\"priest\",\"preventive\",\"mullah\",\"compressive\",\"smokescreen\",\"hibernate\",\"intimidate\",\"leverage\",\"technocratic\",\"performance\",\"factory\",\"abortion\",\"refuel\",\"shroud\",\"suburb\",\"basement\",\"pork\",\"vulcanise\",\"message\",\"empty-handed\",\"gut\",\"cascade\",\"cede\",\"parenthood\",\"helicopter\",\"noticeable\",\"modest\",\"equilibrium\",\"italia\",\"presumption\",\"engage\",\"novel\",\"trait\",\"tape\",\"strip\",\"anytime\",\"crave\",\"hail\",\"indulge\",\"administrator\",\"overtake\",\"trust\",\"herb\",\"dock\",\"musk\"],\"VagueWords\":[],\"MasteredWords\":[]}";
        JSONObject jsonObject = JSONObject.parseObject( jsonstr);
        CikuWord cikuWord = JSONObject.toJavaObject(jsonObject, CikuWord.class);
        System.out.println(cikuWord);
        String jsonstr2=JSONObject.toJSONString(cikuWord);
        System.out.println(jsonstr2);
        String t = Base64.getEncoder().encodeToString(jsonstr2.getBytes(StandardCharsets.UTF_8));
        System.out.println(t);
        String sql2=  " insert into test (id,text)  values (NULL,\""+t+"\")";

        count +=statement.executeUpdate(sql2);
        System.out.println("执行sql成功，一共影响了"+count+"条数据");
        String sql3 =  " select * from test  ";
        ResultSet rs =statement.executeQuery(sql3);

        while(rs.next()){
            System.out.println("查完数据库");
            String json3 =rs.getString("text");
            System.out.println(json3);
            String json4 =new String(Base64.getDecoder().decode(json3),StandardCharsets.UTF_8);
            System.out.println(json4);
            JSONObject jsonObject2 = JSONObject.parseObject( json4);
            CikuWord cikuWord2 = JSONObject.toJavaObject(jsonObject2, CikuWord.class);
            System.out.println(cikuWord2);

            String jsonstr4=JSONObject.toJSONString(cikuWord2);
            System.out.println(jsonstr4);
        }




    }
    //从 数据分表导入合表
    private static void read4() throws Exception {

        for (String initial:sss
        ) {
            String sql= "select * from  word_"+initial+" ";
            ResultSet rs = statement.executeQuery(sql);
            Statement statement2 = conn.createStatement();
            while(rs.next()){
                String sql2=  " insert into word (id,word,charac,trans,soundmark1,soundmark2,deleted,createtime,updatetime)  values (NULL,\""+ rs.getString("word")+"\",'"+ rs.getString("charac")+"',\""+ rs.getString("trans")+"\",\""+ rs.getString("soundmark1")+"\",\""+ rs.getString("soundmark2")+"\",0,'"+ rs.getString("createtime")+"','"+ rs.getString("updatetime")+"');  \n";

                count += statement2.executeUpdate(sql2);
                System.out.println("执行sql成功，一共影响了"+count+"条数据");
            }
            if (statement2!=null){
                statement2.close();
            }

        }



    }
    //从 数据库 导入单词 进行下载音频
    private static void read3() throws Exception {

        for (String initial:sss
             ) {
            String sql= "select * from  word_"+initial+" ";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
              String word=  rs.getString("word");
              String basePath1 = TestImport.class.getClassLoader().getResource("static/audio/").getPath() +"1/"+initial+"/"+word+".mp3";
              //读文件流；
              File dest = new File(basePath1);
              // 检测是否存在文件
              if (!dest.exists()) {
                  // 检测是否存在目录
                  if (!dest.getParentFile().exists()) {
                      System.out.println("不存在1");
                      dest.getParentFile().mkdirs();// 新建文件夹
                  }
                  String netUrl1 ="https://dict.youdao.com/dictvoice?type="+"1"+"&audio="+URLEncoder.encode(word,"UTF-8");
                  fileUtil.downloadFile(netUrl1,basePath1);

              }else{
                System.out.println("存在1");
              }


                String basePath2 = TestImport.class.getClassLoader().getResource("static/audio/").getPath() +"2/"+initial+"/"+word+".mp3";
                //读文件流；
                 dest = new File(basePath2);
                // 检测是否存在文件
                if (!dest.exists()) {
                    // 检测是否存在目录
                    if (!dest.getParentFile().exists()) {
                        System.out.println("不存在2");
                        dest.getParentFile().mkdirs();// 新建文件夹
                    }
                    String netUrl2 ="https://dict.youdao.com/dictvoice?type="+"2"+"&audio="+URLEncoder.encode(word,"UTF-8");
                    fileUtil.downloadFile(netUrl2,basePath2);

                }else{
                    System.out.println("存在2");
                }

            }

        }



    }

    private static void downloadProunceMp3(String base) throws Exception {

        for (String w:wordsEasy
             ) {
            System.out.println("正在下载第"+(count++)+"条数据，当前为："+w)                                                                                                                                                                              ;
            String filePath1= TestImport.class.getClassLoader().getResource("static/audio/").getPath() +"1/"+base+"/"+w+".mp3" ;
            String netUrl1 ="https://dict.youdao.com/dictvoice?type="+"1"+"&audio="+URLEncoder.encode(w,"UTF-8");
            fileUtil.downloadFile(netUrl1,filePath1);
            String filePath2= TestImport.class.getClassLoader().getResource("static/audio/").getPath() +"2/"+base+"/"+w+".mp3" ;
            String netUrl2 ="https://dict.youdao.com/dictvoice?type="+"2"+"&audio="+URLEncoder.encode(w,"UTF-8");
            fileUtil.downloadFile(netUrl2,filePath2);
        }


    }
    //从 word.txt 导入单词 进行下载音频
    public static void read2() throws Exception {

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
                    String word =line.substring(line.indexOf("#")+1,line.indexOf("$")).trim();
                    String newBase =word.substring(0,1).toLowerCase().trim();
                    if (!base.equals(newBase)){
                        //数据库

                        downloadProunceMp3(base);

                        base=newBase;
                        wordsEasy = new ArrayList<>();
                    }

                    if (word.equals("yurt")){
                        flags=true;
                    }
                    if (flags){
//                        System.out.println(word);
                        wordsEasy.add(word);
                    }
                    //最后Z的单词
                    if (line.contains("@end")){
                        downloadProunceMp3(newBase);

                        base=newBase;
                        wordsEasy = new ArrayList<>();
                    }

                }

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
